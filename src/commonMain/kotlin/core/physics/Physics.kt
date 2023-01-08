package core.physics

import kotlin.math.*

fun dist(x1: Double, y1: Double, x2: Double, y2: Double, root: Boolean = true): Double {
    val t1 = x1 - x2
    val t2 = y1 - y2
    val ans = t1*t1 + t2*t2
    return if(root) sqrt(ans) else ans
}

fun distLessThan(x1: Double, y1: Double, x2: Double, y2: Double, offset: Double): Boolean {
    val t1 = x1 - x2
    val t2 = y1 - y2
    val ans = t1*t1 + t2*t2
    return ans < offset*offset
}

fun withinSquare(playerPos: Pair<Double, Double>, itemPos: Pair<Double, Double>)
    = playerPos.first > itemPos.first - 23.0
        && playerPos.first < itemPos.first+23.0
        && playerPos.second > itemPos.second-23.0
        && playerPos.second < itemPos.second+23.0
