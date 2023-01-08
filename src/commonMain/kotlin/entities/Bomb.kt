package entities

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import core.base.*
import core.physics.*
import entities.tiles.*
import load.*
import ui.level.*

val pst = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

class Bomb(val layer: Layer, private val col: Int, private val row: Int)
    : OImage(BitmapDB.getBitmap("items/icebomb.png")) {

    init{
        anchor(0.5, 0.5)
        this.x = col*45.0 + 22.0
        this.y = row*45.0 + 22.0
        layer.addTile(col, row, this)
    }
    suspend fun ticking(player: Player) {
        this.tween(::scale[0.7, 0.5], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.5, 0.7], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.7, 0.5], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.5, 0.8], time = 0.8.seconds, easing = Easing.EASE_IN_OUT)
        player.releaseBomb()
        blast(player)
        layer.removeTile(col, row)
    }
    private suspend fun blast(player: Player) {
        val stoneLayer = layer.world.getLayer("stone")
        val dir = arrayOf(false, false, false, false)
        val r = player.getBlastRange()
        var safe = true

        Flame(layer, this@Bomb.col, this@Bomb.row).apply {
            x = this@Bomb.col*45.0 + 22.0
            y = this@Bomb.row*45.0 + 22.0
            if(withinSquare(player.loc, this.loc)) {
                safe = false
                player.decreaseHP()
            }
        }
        for(c in 1 .. r) {
            delay(100.milliseconds)
            for((i, p) in pst.withIndex()) {
                if(dir[i]) continue
                val tempX = (col + p.first*c)
                val tempY = (row + p.second*c)
                if(stoneLayer.occupied(tempX, tempY)) {
                    dir[i] = true
                    if(stoneLayer[tempX, tempY] !is Stone) {
                        stoneLayer.removeTile(tempX, tempY)
                    }
                }
                Flame(layer, tempX, tempY).apply {
                    if (withinSquare(player.loc, this.loc) && safe) {
                        safe = false
                        player.decreaseHP()
                    }
                }
            }
        }
    }
}

inline fun Layer.bomb(col: Int, row: Int, callback: @ViewDslMarker Bomb.() -> Unit = {}): Bomb {
    return Bomb(this, col, row).apply(callback)
}
