package entities.dynamics

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korim.color.*
import com.soywiz.korio.async.*
import com.soywiz.korma.interpolation.*
import core.algorithm.*
import core.base.*
import core.physics.*
import entities.base.*
import entities.bomb.*
import entities.statics.*
import ui.level.*

abstract class Person(val world: World, protected val animates: SpriteDirections): AnimationObj(animates.down) {
    abstract var hitPoint: Int
    abstract var speed: Double
    var alive = true
    var immune = false
    protected var frozen = false

    override fun collideWith(other: GameObject, type: CollisionType)
        = when(type) {
            CollisionType.REC_REC -> { Collider.collideBetweenRectangles(this.boundRectangle(), other.boundRectangle()) }
            CollisionType.CIRCLE_CIRCLE -> { Collider.collideBetweenCircles(this.boundCircle(20.0), other.boundCircle(20.0)) }
            CollisionType.MIX -> {Collider.collideBetweenCircleAndRectangle(this.boundCircle(20.0), other.boundRectangle()) }
        }

    fun move(dir: MoveDirection) {
        when(dir) {
            MoveDirection.LEFT -> {
                x -= speed
                play(animates.left)
            }
            MoveDirection.RIGHT -> {
                x += speed
                play(animates.right)
            }
            MoveDirection.UP -> {
                y -= speed
                play(animates.up)
            }
            MoveDirection.DOWN -> {
                y += speed
                play(animates.down)
            }
        }
    }

    protected fun feasibleDirection(delta: Double, tiles: List<Tile>): BooleanArray {
        val ret = booleanArrayOf(true, true, true, true)
        for(tile in tiles) {
            if(tile is Bomb) {
                if(Collider.collideBetweenCircleAndRectangle(makeCircle(x, y-delta, 18.0), tile.boundRectangle())) {
                    if(tile.lockMove) ret[2] = false
                } else tile.lockMove = true
                if(Collider.collideBetweenCircleAndRectangle(makeCircle(x, y+delta, 18.0), tile.boundRectangle())) {
                    if(tile.lockMove) ret[3] = false
                } else tile.lockMove = true
                if(Collider.collideBetweenCircleAndRectangle(makeCircle(x-delta, y, 18.0), tile.boundRectangle())) {
                    if(tile.lockMove) ret[0] = false
                } else tile.lockMove = true
                if(Collider.collideBetweenCircleAndRectangle(makeCircle(x+delta, y, 18.0), tile.boundRectangle())) {
                    if(tile.lockMove) ret[1] = false
                } else tile.lockMove = true
                continue
            }
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

    abstract suspend fun takeDamage(damage: Int, freeze: Boolean = false)

    suspend fun freeze() {
        frozen = true
        instance.tween(instance::colorMul[Colors["#ffffff"], Colors["#00b2ff88"]], time = 100.milliseconds, easing = Easing.EASE_IN_OUT)
        delay(1300.milliseconds)
        instance.tween(instance::colorMul[Colors["#00b2ff88"], Colors["#ffffff"]], time = 100.milliseconds, easing = Easing.EASE_IN_OUT)
        frozen = false
    }

    fun square(): Square {
        val row = (this.y/45.0).toInt()
        val col = (this.x/45.0).toInt()
        return Square(col, row)
    }
}
