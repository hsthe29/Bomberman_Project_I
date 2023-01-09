package entities

import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import core.physics.*
import entities.tiles.*
import load.*
import ui.level.*
import kotlin.coroutines.*

class Flame(val layer: Layer, private val src: Bomb, val col: Int, val row: Int): Sprite() {
    private val spriteMap = BitmapDB.getBitmap("items/explosion_red.png")


    private val animation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 45,
        spriteHeight = 45,
        marginTop = 0,
        marginLeft = 0,
        columns = 10,
        rows = 2,
        byRows = false
    )

    /* Get location of this component, this method equivalent to View.pos
       but return Pair<Double, Double> instead of IPoint */
    val loc: Pair<Double, Double>
        get() = x to y

    init {
        addTo(layer)
        anchor(0.5, 0.5)
        x = col*45.0 + 22.0
        y = row*45.0 + 22.0
    }

    suspend fun execute(player: Player, dir :Int? = null) {
        launch(coroutineContext) {
            if(!src.dealtDamage && withinSquare(player.loc, this.loc)) {
                launch(coroutineContext) {
                    src.dealtDamage = true
                    player.decreaseHP()
                }
            }
            launch(coroutineContext) {
                playAnimation(animation, spriteDisplayTime = 50.milliseconds)
                onAnimationStopped.invoke {
                    removeFromParent()
                }
            }
            launch(coroutineContext) {
                val stoneLayer = layer.world.getLayer("stone")
                if(stoneLayer.occupied(col, row)) {
                    dir?.let{ src.block[it] = true }
                    if(stoneLayer[col, row] !is Stone) { stoneLayer[col, row] = null }
                }
            }
        }
    }
}
