package entities.base

interface Attackable {
    var attack: Int

    fun dealDamage(): Int
}
