package entities.base

interface Attackable {
    var hitPoint: Int
    var attack: Int

    fun dealDamage(): Int
}
