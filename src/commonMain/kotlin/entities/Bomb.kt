package entities

import bitmapDB
import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import core.base.*
import core.physics.*
import entities.tiles.*
import ui.level.*

val pst = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

class Bomb(val world: World, _x: Double, _y: Double) : OImage(bitmapDB.getBitmap("items/icebomb.png")) {
    // tween(progressBar::ratio[1.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
    private val row = (_y/45.0).toInt()
    private val col = (_x/45.0).toInt()
    init{
        anchor(0.5, 0.5)
        this.x = col*45.0 + 23
        this.y = row*45.0 + 23
        world.putLayer.addTile(col, row, this)
    }
    suspend fun ticking(player: Player) {
        this.tween(::scale[0.7, 0.5], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.5, 0.7], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.7, 0.5], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        this.tween(::scale[0.5, 0.8], time = 0.8.seconds, easing = Easing.EASE_IN_OUT)
        player.releaseBomb()
        blast(player)
        world.putLayer.removeTile(col, row)
    }
    private suspend fun blast(player: Player) {
        val stoneLayer = world.getLayer("stone")
        val prun = arrayOf(false, false, false, false)
        val r = player.getBlastRange()
        var safe = true

        Flame(world, (this@Bomb.x/45.0).toInt(), (this@Bomb.y/45.0).toInt()).apply {
            anchor(0.5, 0.5)
            x = (this@Bomb.x/45.0).toInt()*45.0 + 23.0
            y = (this@Bomb.y/45.0).toInt()*45.0 + 23.0
            if(distLessThan(x, y, player.x, player.y, 20.0)) {
                safe = false
                player.decreaseHP()
            }
        }
        for(c in 1 .. r) {
            delay(100.milliseconds)
            for((i, p) in pst.withIndex()) {
                if(prun[i]) continue
                val tempX = (col + p.first*c)
                val tempY = (row + p.second*c)
                if(stoneLayer.occupied(tempX, tempY)) {
                    prun[i] = true
                    if(stoneLayer[tempX, tempY] !is Stone) {
                        stoneLayer.removeTile(tempX, tempY)
                    }
                }
                val flame = Flame(world, tempX, tempY)
                if(distLessThan(flame.x, flame.y, player.x, player.y, 20.0) && safe) {
                    safe = false
                    player.decreaseHP()
                }
            }
        }
    }
}
