package entities.dynamics.enemy

import com.soywiz.klock.*
import core.base.*
import entities.base.*
import entities.dynamics.*
import kotlin.random.*

abstract class Enemy(animates: SpriteDirections, val kind: MoveKind): Person(animates) {

    protected var direction: MoveDirection

    init {
        direction = if(kind == MoveKind.HORIZONTAL) MoveDirection.LEFT else MoveDirection.DOWN
        play(when(direction) {
            MoveDirection.LEFT -> animates.left
            MoveDirection.DOWN -> animates.down
            else -> { animates.right }
        })
    }
    abstract fun update()

    fun changeDirection() {
        direction = when(kind) {
            MoveKind.HORIZONTAL -> if(direction == MoveDirection.LEFT) MoveDirection.RIGHT else MoveDirection.LEFT
            MoveKind.VERTICAL -> if(direction == MoveDirection.DOWN) MoveDirection.UP else MoveDirection.DOWN
            MoveKind.MIX -> MoveDirection.values()[Random.nextInt(0, 4)]
        }
    }
}
