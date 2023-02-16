package entities.statics.items

import com.soywiz.klock.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import entities.dynamics.*
import load.*
import ui.level.*
import kotlin.coroutines.*

class Key(val layer: Layer,
          info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.KEY
    override val row = info.row
    override val col = info.col
    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

    override suspend fun takeEffect(bomber: Player) {
        val gate = bomber.world.gate
        gate.isOpened = true
        gate.open()
        bomber.world.itemLayer[col, row] = null
        bomber.world.stoneLayer[gate.col, gate.row]?.let {
            launch(coroutineContext) {
                it.instance.animateParallel {
                    sequence(looped = true) {
                        tween(it.instance::colorMul[Colors["#c1f753"]], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                        tween(it.instance::colorMul[Colors["#ffffff"]], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                    }
                }
            }
        }
    }
}

inline fun Layer.key(info: TileInfo, callback: @ViewDslMarker Key.() -> Unit = {}): Key {
    return Key(this, info).apply(callback)
}

