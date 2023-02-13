package entities.bomb

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import core.base.*
import entities.dynamics.*
import entities.statics.*
import entities.statics.items.*
import kotlinx.coroutines.*
import load.*
import ui.level.*
import kotlin.coroutines.*

val directions = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

class Bomb(val layer: Layer, override val col: Int, override val row: Int)
    : Item(VfsDB.getBitmap("items/bomb.png")) {
    private var triggered = false
    var lockMove = false
    val block = arrayOf(false, false, false, false)
    override suspend fun takeEffect(bomber: Bomber) {
        TODO("Not yet implemented")
    }

    override val type = TileType.NONE
    init{
        layer[col, row] = this
        this.x = col*45.0 + 22.0
        this.y = row*45.0 + 22.0
    }
    suspend fun ticking(player: Bomber) {
        instance.tween(instance::scale[0.7, 0.6], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        instance.tween(instance::scale[0.6, 0.7], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        instance.tween(instance::scale[0.7, 0.6], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        instance.tween(instance::scale[0.6, 0.8], time = 0.85.seconds, easing = Easing.EASE_IN_OUT)
        trigger(player)
    }

    private suspend fun blast(player: Bomber) {
        val r = player.explosionRadius
        CoroutineScope(coroutineContext).launch(start=CoroutineStart.DEFAULT) {
            Flame(layer,this@Bomb, this@Bomb.col, this@Bomb.row).execute(player)
        }
        for(c in 1 .. r) {
            delay(80.milliseconds)
            val jobs = arrayListOf<Job>()
            for((dir, loc) in directions.withIndex()) {
                if(block[dir]) continue
                val _col = (col + loc.first*c)
                val _row = (row + loc.second*c)
                val flame = Flame(layer, this, _col, _row)
                jobs += CoroutineScope(coroutineContext).launch(start=CoroutineStart.LAZY) { flame.execute(player, dir = dir) }
                if(layer[_col, _row] is Bomb ) {
                    val bomb = layer[_col, _row] as Bomb
                    jobs += CoroutineScope(coroutineContext)
                        .launch(start=CoroutineStart.LAZY) { bomb.trigger(player) }
                }
            }
            jobs.forEach{ it.start() }
        }
    }

    private suspend fun trigger(player: Bomber) {
        if(triggered) return
        VfsDB.getSound("sound/sfx/bomb_explode.mp3").play()
        triggered = true
        player.releaseBomb()
        layer[col, row] = null
        blast(player)
    }
}

inline fun Layer.bomb(col: Int, row: Int, callback: @ViewDslMarker Bomb.() -> Unit = {}): Bomb {
    return Bomb(this, col, row).apply(callback)
}
