package entities.bomb

import com.soywiz.korge.view.*
import entities.base.*
import load.*
import ui.level.*

abstract class Flame(
    val layer: Layer, protected val source: Bomb,
    val dir: Int,
    val col: Int, val row: Int,
    protected val animation: SpriteAnimation
): AnimationObj(animation) {

    init {
        instance.addTo(layer.world.explosionLayer)
        anchor(0.5, 0.5)
        x = col*45.0 + 22.0
        y = row*45.0 + 22.0
    }

    val loc: Pair<Double, Double>
        get() = x to y

    abstract suspend fun fire()
    override fun collideWith(other: GameObject, type: CollisionType): Boolean {
        TODO("Not yet implemented")
    }
}
