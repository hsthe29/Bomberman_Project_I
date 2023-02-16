package entities.bomb

import com.soywiz.klock.*
import com.soywiz.korma.annotations.*
import entities.base.*
import kotlinx.coroutines.*
import ui.level.*
import kotlin.coroutines.*

class MagicBomb(layer: Layer, owner: Bomber, col: Int, row: Int)
    : Bomb(layer, owner, col, row,url = "items/frostbomb.png") {

    override suspend fun blast() {
        val r = owner.explosionRadius
        for(c in 1 .. r) {
            com.soywiz.korio.async.delay(80.milliseconds)
            val jobs = arrayListOf<Job>()
            for((dir, loc) in directions.withIndex()) {
                if(block[dir]) continue
                val _col = (col + loc.first*c)
                val _row = (row + loc.second*c)
                val flame = MagicFlame(layer, this, dir, _col, _row)
                jobs += CoroutineScope(coroutineContext).launch(start= CoroutineStart.LAZY) { flame.fire() }
                if(layer[_col, _row] is ExplosiveBomb ) {
                    val bomb = layer[_col, _row] as Bomb
                    jobs += CoroutineScope(coroutineContext)
                        .launch(start= CoroutineStart.LAZY) { bomb.trigger() }
                }
            }
            jobs.forEach{ it.start() }
        }
    }
}

inline fun Layer.magicBomb(owner: Bomber, col: Int, row: Int, callback: @ViewDslMarker MagicBomb.() -> Unit = {}): MagicBomb {
    return MagicBomb(this, owner, col, row).apply(callback)
}

