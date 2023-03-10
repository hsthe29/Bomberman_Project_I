package entities.dynamics.enemy

import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import core.algorithm.*
import core.base.*
import core.physics.*
import entities.base.*
import ui.level.*
import kotlin.coroutines.*

suspend inline fun World.skeleton(kind: MoveKind, callback: @ViewDslMarker Skeleton.() -> Unit = {}): Skeleton {
    val spriteMap = resourcesVfs["character/skeleton.png"].readBitmap()
    val skeleton = Skeleton(this, skeletonAnimations(spriteMap), kind).apply(callback)
    skeleton.instance.addTo(this)
    return skeleton
}

class Skeleton(world: World, animates: SpriteDirections, kind: MoveKind): Enemy(world, animates, kind), Attackable {
    override var hitPoint = 6
    override var attack = 2
    override var speed = 2.0
    private val baseSpeed = 0.5
    private var last = System.currentTimeMillis() // milliseconds
    private var paths = ArrayDeque<SquareCoordinate>()

    override fun dealDamage() = attack
    override suspend fun update() {
        if(frozen) return
        val now = System.currentTimeMillis()
        if(now - last > 1500) {
            val pathJob = async(coroutineContext) {
                PathFinder.evalShortestPath(world.stoneLayer.export(), this.square(), world.player.square())
            }
            paths = pathJob.await()
            last = System.currentTimeMillis()
        }

        if(paths.isNotEmpty()) {
            val first = paths.first()
            if(distLessThan(x, y, first.col, first.row, 3.0)) {
                paths.removeFirst()
                speed = baseSpeed + 1.0/(paths.size + 1)*0.8
            }
            if(first.col - x >= 0.2) move(MoveDirection.RIGHT)
            else if(first.col - x <= -0.2) move(MoveDirection.LEFT)
            else if(first.row - y >= 0.2) move(MoveDirection.DOWN)
            else if(first.row - y <= -0.2) move(MoveDirection.UP)
        }

        if(distLessThan(x, y, world.player.x, world.player.y, 15.0)) {
            launch(coroutineContext) {
                world.player.takeDamage(dealDamage())
            }
        }
    }
}
