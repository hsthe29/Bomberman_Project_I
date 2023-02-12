package entities.dynamics.enemy

import com.soywiz.korev.*
import com.soywiz.korio.async.*
import core.base.*
import entities.base.*
import entities.bomb.*
import ui.level.*
import kotlin.coroutines.*

class Ghost(val world: World, animates: SpriteDirections, kind: MoveKind): Enemy(animates, kind) {
    override var hitPoint = 3
    override var attack = 2
    override var speed = 1.5

    override fun update() {
        var anyMovement = false
        val tiles = world.allTilesWithin(x, y)
        val (left, right, up, down) = feasibleDirection(speed, tiles)

        when(direction) {
            MoveDirection.LEFT -> {
                if(left)
                    move(MoveDirection.LEFT)
                else direction = MoveDirection.RIGHT
            }
            MoveDirection.DOWN -> {
                if(down)
                    move(MoveDirection.DOWN)
                else direction = MoveDirection.UP
            }
            MoveDirection.UP -> {
                if(up)
                    move(MoveDirection.UP)
                else direction = MoveDirection.DOWN
            }
            MoveDirection.RIGHT -> {
                if(right)
                    move(MoveDirection.RIGHT)
                else direction = MoveDirection.LEFT
            }
        }
    }

    override fun move(dir: MoveDirection) {
        when(dir) {
            MoveDirection.LEFT -> {
                x -= speed
            }
            MoveDirection.RIGHT -> {
                x += speed
            }
            MoveDirection.UP -> {
                y -= speed
            }
            MoveDirection.DOWN -> {
                y += speed
            }
        }
    }
}
