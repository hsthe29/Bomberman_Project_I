package entities

import com.soywiz.klock.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import core.base.*
import core.physics.*
import entities.tiles.*
import kotlinx.coroutines.*
import load.*
import ui.level.*
import kotlin.coroutines.*

val directions = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

class Bomb(val layer: Layer, private val col: Int, private val row: Int)
    : OImage(BitmapDB.getBitmap("items/icebomb.png")) {
    private var triggered = false
    var dealtDamage = false
    val block = arrayOf(false, false, false, false)
    init{
        layer[col, row] = this
        anchor(0.5, 0.5)
        this.x = col*45.0 + 22.0
        this.y = row*45.0 + 22.0
    }
    suspend fun ticking(player: Player) {
        this.tween(::scale[0.7, 0.5], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.5, 0.7], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.7, 0.5], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.5, 0.8], time = 0.85.seconds, easing = Easing.EASE_IN_OUT)
        trigger(player)
    }

    private suspend fun blast(player: Player) {
        val r = player.getBlastRange()
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
                if(layer.occupied(_col, _row)) {
                    val bomb = layer[_col, _row] as Bomb
                    jobs += CoroutineScope(coroutineContext)
                        .launch(start=CoroutineStart.LAZY) { bomb.trigger(player) }
                }
            }
            jobs.forEach{ it.start() }
        }
    }

    private suspend fun trigger(player: Player) {
        if(triggered) return
        triggered = true
        player.releaseBomb()
        layer[col, row] = null
        blast(player)
    }
}

inline fun Layer.bomb(col: Int, row: Int, callback: @ViewDslMarker Bomb.() -> Unit = {}): Bomb {
    return Bomb(this, col, row).apply(callback)
}
