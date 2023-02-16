package entities.bomb

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import entities.base.*
import entities.dynamics.*
import entities.statics.items.*
import kotlinx.coroutines.*
import load.*
import ui.level.*
import kotlin.coroutines.*

class FrostBomb(layer: Layer, owner: Bomber, col: Int, row: Int)
    : Bomb(layer, owner, col, row,url = "items/frostbomb.png") {

    override suspend fun blast() {
        val r = owner.explosionRadius
        CoroutineScope(coroutineContext).launch(start=CoroutineStart.DEFAULT) {
            FrostFlame(
                layer = layer,
                source = this@FrostBomb,
                dir = -1,
                col = this@FrostBomb.col,
                row = this@FrostBomb.row).fire()
        }
        for(c in 1 .. r) {
            delay(80.milliseconds)
            val jobs = arrayListOf<Job>()
            for((dir, loc) in directions.withIndex()) {
                if(block[dir]) continue
                val _col = (col + loc.first*c)
                val _row = (row + loc.second*c)
                val flame = FrostFlame(layer, this, dir, _col, _row)
                jobs += CoroutineScope(coroutineContext).launch(start=CoroutineStart.LAZY) { flame.fire() }
                if(layer[_col, _row] is Bomb ) {
                    val bomb = layer[_col, _row] as Bomb
                    jobs += CoroutineScope(coroutineContext)
                        .launch(start=CoroutineStart.LAZY) { bomb.trigger() }
                }
            }
            jobs.forEach{ it.start() }
        }
    }
}

inline fun Layer.frostBomb(owner: Bomber, col: Int, row: Int, callback: @ViewDslMarker FrostBomb.() -> Unit = {}): FrostBomb {
    return FrostBomb(this, owner, col, row).apply(callback)
}
