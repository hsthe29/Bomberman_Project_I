package entities.dynamics.enemy

import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import core.algorithm.*
import core.base.*
import core.physics.*
import entities.base.*
import entities.bomb.*
import ui.level.*
import kotlin.coroutines.*

suspend inline fun World.boss(kind: MoveKind, callback: @ViewDslMarker Boss.() -> Unit = {}): Boss {
    val spriteMap = resourcesVfs["character/boss.png"].readBitmap()
    val boss = Boss(this, skeletonAnimations(spriteMap), kind).apply(callback)
    boss.instance.addTo(this)
    return boss
}

class Boss(world: World, animates: SpriteDirections, kind: MoveKind,

) : Enemy(world, animates, kind), Bomber  {
    override var attack = 3
    override var type: BombType = BombType.FROST
    override var explosionRadius = 6
    override var hitPoint = 11
    override var speed = 0.6
    private val baseSpeed = 0.6
    private var last = System.currentTimeMillis() // milliseconds
    private var putBombTime = 0L
    private var paths = ArrayDeque<SquareCoordinate>()
    override suspend fun putBomb() {
        val now = System.currentTimeMillis()
        if(now - putBombTime > 1500) { // 1.5 secs
            val row = (this.y/45.0).toInt()
            val col = (this.x/45.0).toInt()
            if(!world.stoneLayer.occupied(col, row)) {
                world.stoneLayer.magicBomb(this, col, row) {
                    trigger()
                }
                putBombTime = now
            }
        }
    }

    override fun releaseBomb() {
        TODO("Not yet implemented")
    }

    override fun dealDamage() = attack

    override suspend fun update() {
        if(paths.size < 8) launch(coroutineContext) { putBomb() }
        val now = System.currentTimeMillis()
        if(now - last > 2000) {
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
