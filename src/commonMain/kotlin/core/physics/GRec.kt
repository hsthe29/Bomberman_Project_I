package core.physics

data class GRec(val ex: Double, val wx: Double, val ny: Double, val sy: Double)

fun makeRectangle(ex: Double, wx: Double, ny: Double, sy: Double) = GRec(ex, wx, ny, sy)
