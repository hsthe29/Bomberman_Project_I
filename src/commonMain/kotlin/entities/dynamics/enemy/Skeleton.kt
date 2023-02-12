package entities.dynamics.enemy

import core.base.*
import entities.base.*

class Skeleton(animates: SpriteDirections, kind: MoveKind): Enemy(animates, kind) {
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
