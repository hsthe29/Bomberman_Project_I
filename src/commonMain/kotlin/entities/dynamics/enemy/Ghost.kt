package entities.dynamics.enemy

import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import core.base.*
import core.physics.*
import entities.base.*
import ui.level.*
import kotlin.coroutines.*

suspend inline fun World.ghost(kind: MoveKind, callback: @ViewDslMarker Ghost.() -> Unit = {}): Ghost {
    val spriteMap = resourcesVfs["character/ghost.png"].readBitmap()
    val ghost = Ghost(this, ghostAnimations(spriteMap), kind).apply(callback)
    ghost.instance.addTo(this)
    return ghost
}

class Ghost(world: World, animates: SpriteDirections, kind: MoveKind): Enemy(world, animates, kind), Attackable {
    override var hitPoint = 3
    override var attack = 1
    override var speed = 0.9

    override fun dealDamage() = attack

    override suspend fun update() {
        if(frozen) return
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
        if(distLessThan(x, y, world.player.x, world.player.y, 15.0)) {
            launch(coroutineContext) {
                world.player.takeDamage(dealDamage())
            }
        }
    }
}
