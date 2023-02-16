package entities.bomb

import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import core.physics.*
import entities.base.*
import entities.dynamics.*
import entities.statics.tiles.*
import load.*
import ui.level.*
import kotlin.coroutines.*

class FrostFlame(layer: Layer, source: FrostBomb, dir: Int, col: Int, row: Int,
                 animation: SpriteAnimation = explosionAnimation(VfsDB.getBitmap("items/explosion_frost.png")))
    : Flame(layer, source, dir, col, row, animation){

    override suspend fun fire() {
        val player = layer.world.player
        launch(coroutineContext) {
            if(withinSquare(Pair(player.x, player.y), this.loc)) {
                launch(coroutineContext) {
                    player.takeDamage(source.owner.dealDamage(), freeze = true)
                }
            }

            launch(coroutineContext) {
                for (e in layer.world.enemies)
                    if(e.alive && withinSquare(Pair(e.x, e.y), this.loc)) {
                        launch(coroutineContext) {
                            e.takeDamage(player.dealDamage(), freeze = true)
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
                val stoneLayer = layer.world.stoneLayer
                if(stoneLayer.occupied(col, row)) {
                    if(dir > -1)
                    source.block[dir] = true
                    if(stoneLayer[col, row] is Brick) { stoneLayer[col, row] = null }
                }
            }
        }
    }

    override fun collideWith(other: GameObject, type: CollisionType): Boolean {
        TODO("Not yet implemented")
    }
}
