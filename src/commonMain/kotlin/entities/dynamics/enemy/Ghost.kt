package entities.dynamics.enemy

import com.soywiz.korev.*
import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import core.base.*
import entities.base.*
import entities.bomb.*
import entities.dynamics.*
import ui.level.*
import kotlin.coroutines.*

suspend inline fun World.ghost(kind: MoveKind, callback: @ViewDslMarker Ghost.() -> Unit = {}): Ghost {
    val spriteMap = resourcesVfs["character/ghost.png"].readBitmap()
    val ghost = Ghost(this, ghostAnimations(spriteMap), kind).apply(callback)
    ghost.instance.addTo(this)
    return ghost
}

class Ghost(val world: World, animates: SpriteDirections, kind: MoveKind): Enemy(animates, kind) {
    override var hitPoint = 3
    override var attack = 2
    override var speed = 0.8

    override fun update() {
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
}
