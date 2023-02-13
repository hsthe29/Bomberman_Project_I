package entities.dynamics.enemy

import com.soywiz.korge.view.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import core.base.*
import entities.base.*
import ui.level.*

suspend inline fun World.skeleton(kind: MoveKind, callback: @ViewDslMarker Skeleton.() -> Unit = {}): Skeleton {
    val spriteMap = resourcesVfs["character/skeleton.png"].readBitmap()
    val skeleton = Skeleton(this, skeletonAnimations(spriteMap), kind).apply(callback)
    skeleton.instance.addTo(this)
    return skeleton
}

class Skeleton(world: World, animates: SpriteDirections, kind: MoveKind): Enemy(world, animates, kind) {
    override var hitPoint = 5
    override var attack = 3
    override var speed = 2.0

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun move(dir: MoveDirection) {
        TODO("Not yet implemented")
    }
}
