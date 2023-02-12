package entities.base

import com.soywiz.korge.view.*
import core.physics.*

abstract class GameObject: Collidable {
    abstract val instance: BaseImage
    var x: Double
        get() = instance.x
        set(value) {
            instance.x = value
        }
    var y: Double
        get() = instance.y
        set(value) {
            instance.y = value
        }
    val scale: Double
        get() = instance.scale
    val scaleX: Double
        get() = instance.scaleX
    val scaleY: Double
        get() = instance.scaleY
    val anchorX: Double
        get() = instance.anchorX
    val anchorY: Double
        get() = instance.anchorY
    fun xy(x: Int, y: Int) = instance.xy(x, y)

    fun anchor(anchorX: Double, anchorY: Double = anchorX) = instance.anchor(anchorX, anchorY)

    fun scale(sclX: Double, sclY: Double = sclX) = instance.scale(sclX, sclY)

    fun boundRectangle(): GRec {
        val wx = x - anchorX*instance.scaleX*instance.width
        val ex = wx + instance.width*instance.scaleX
        val ny = y - anchorY*instance.scaleY*instance.height
        val sy = ny + instance.height*instance.scaleY
        return makeRectangle(ex = ex, wx = wx, ny = ny, sy = sy)
    }

    fun boundCircle(radius: Double) = makeCircle(centerX = x, centerY = y, radius = radius)

    fun removeFromParent() {
        instance.removeFromParent()
    }

}
