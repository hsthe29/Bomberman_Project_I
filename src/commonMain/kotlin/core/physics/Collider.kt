package core.physics

import kotlin.math.*

object Collider {

    inline fun distBetween(point1: Pair<Double, Double>, point2: Pair<Double, Double>): Double {
        val deltaX = point1.first - point2.first
        val deltaY = point1.second - point2.second
        return java.lang.Math.sqrt(deltaX*deltaX + deltaY*deltaY)
    }
    fun collideBetweenRectangles(rec1: GRec, rec2: GRec): Boolean {
        return rec1.wx <= rec2.ex && rec1.ex >= rec2.wx && rec1.ny <= rec2.sy && rec1.sy >= rec2.ny
    }

    fun collideBetweenCircles(cir1: GCir, cir2: GCir): Boolean {
        return cir1.radius+cir2.radius >= distBetween(Pair(cir1.centerX, cir1.centerY), Pair(cir2.centerX, cir2.centerY))
    }

    fun collideBetweenCircleAndRectangle(cir: GCir, rec: GRec): Boolean {
        val bottomRight = Pair(rec.ex, rec.sy)
        val upperLeft = Pair(rec.wx, rec.ny)
        val xN = max(upperLeft.first, min(cir.centerX, bottomRight.first))
        val yN = max(upperLeft.second, min(cir.centerY, bottomRight.second))
        val dX = xN - cir.centerX
        val dY = yN - cir.centerY
        return (dX*dX + dY*dY) <= cir.radius*cir.radius
    }
}
