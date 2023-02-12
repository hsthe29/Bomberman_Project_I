package entities.dynamics

import com.soywiz.korge.view.*
import core.base.*
import core.physics.*
import entities.base.*
import entities.statics.*

abstract class Person(protected val animates: SpriteDirections): AnimationObj(animates.down), Attackable {
    abstract var speed: Double

    override fun dealDamage(): Int {
        return attack
    }
    override fun collideWith(other: GameObject, type: CollisionType)
        = when(type) {
            CollisionType.REC_REC -> { Collider.collideBetweenRectangles(this.boundRectangle(), other.boundRectangle()) }
            CollisionType.CIRCLE_CIRCLE -> { Collider.collideBetweenCircles(this.boundCircle(20.0), other.boundCircle(20.0)) }
            CollisionType.MIX -> {Collider.collideBetweenCircleAndRectangle(this.boundCircle(20.0), other.boundRectangle()) }
        }
    abstract fun move(dir: MoveDirection)

    protected fun feasibleDirection(delta: Double, tiles: List<Tile>): BooleanArray {
        val ret = booleanArrayOf(true, true, true, true)
        for(tile in tiles) {
            if(Collider.collideBetweenCircleAndRectangle(makeCircle(x, y-delta, 20.0), tile.boundRectangle()))
                ret[2] = false
            if(Collider.collideBetweenCircleAndRectangle(makeCircle(x, y+delta, 20.0), tile.boundRectangle()))
                ret[3] = false
            if(Collider.collideBetweenCircleAndRectangle(makeCircle(x-delta, y, 20.0), tile.boundRectangle()))
                ret[0] = false
            if(Collider.collideBetweenCircleAndRectangle(makeCircle(x+delta, y, 20.0), tile.boundRectangle()))
                ret[1] = false
        }
        return ret
    }

}
