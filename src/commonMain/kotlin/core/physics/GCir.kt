package core.physics

data class GCir(val centerX: Double, val centerY: Double, val radius: Double)

fun makeCircle(centerX: Double, centerY: Double, radius: Double) = GCir(centerX, centerY, radius)
