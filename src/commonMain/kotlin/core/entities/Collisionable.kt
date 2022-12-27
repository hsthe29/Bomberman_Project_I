package core.entities

import core.*

interface Collisionable {
    var center: CustomPoint2D
    var radius: Double
    var bottomRight: CustomPoint2D
    var upperLeft: CustomPoint2D

    fun collidesWith(other: Collisionable): Boolean
    fun collidesWith(otherList: List<Collisionable>): Boolean
}
