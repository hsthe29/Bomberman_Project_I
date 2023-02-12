package entities.base

interface Collidable {
    fun collideWith(other: GameObject, type: CollisionType): Boolean
}

enum class CollisionType {
    REC_REC, CIRCLE_CIRCLE, MIX
}
