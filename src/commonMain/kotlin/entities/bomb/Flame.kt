package entities.bomb

import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import core.physics.*
import entities.base.*
import entities.dynamics.*
import entities.statics.tiles.*
import load.*
import ui.level.*
import kotlin.coroutines.*

class Flame(val layer: Layer, private val src: Bomb, val col: Int, val row: Int, private val animation: SpriteAnimation = redExplosionAnimation(VfsDB.getBitmap("items/explosion_red.png"))): AnimationObj(animation) {


    val loc: Pair<Double, Double>
        get() = x to y

    init {
        instance.addTo(layer.world.explosionLayer)
        anchor(0.5, 0.5)
        x = col*45.0 + 22.0
        y = row*45.0 + 22.0
    }

    suspend fun execute(player: Bomber, dir :Int? = null) {
        launch(coroutineContext) {
            if(withinSquare(Pair(player.x, player.y), this.loc)) {
                launch(coroutineContext) {
                    player.takeDamage(player.dealDamage())
                }
            }

            launch(coroutineContext) {
                for (e in player. world.enemies)
                    if(e.alive && withinSquare(Pair(e.x, e.y), this.loc)) {
                        launch(coroutineContext) {
                            e.takeDamage(player.dealDamage())
                        }
                    }
            }

            launch(coroutineContext) {
                play(animation, spriteDisplayTime = 50.milliseconds)
                onAnimationCompleted.invoke {
                    removeFromParent()
                }
            }
            launch(coroutineContext) {
                val stoneLayer = layer.world.getLayer("stone")
                if(stoneLayer.occupied(col, row)) {
                    dir?.let{ src.block[it] = true }
                    if(stoneLayer[col, row] is Brick) { stoneLayer[col, row] = null }
                }
            }
        }
    }

    override fun collideWith(other: GameObject, type: CollisionType): Boolean {
        TODO("Not yet implemented")
    }
}
