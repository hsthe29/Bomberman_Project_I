package entities.base

enum class BombType {
    EXPLOSION, FROST
}

interface Bomber: Attackable {

    var type: BombType

    var explosionRadius: Int


    suspend fun putBomb()

    fun releaseBomb()
}
