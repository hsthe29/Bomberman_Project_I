package entities.tiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korio.resources.*
import core.base.*
import load.*
import ui.level.*

class Brick(bitmap: Resourceable<out BaseBmpSlice>,
           anchorX: Double = 0.0,
           anchorY: Double=0.0): OImage(bitmap = bitmap, anchorX = anchorX, anchorY = anchorY) {
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.0,
        anchorY: Double = 0.0,
    ) : this(bitmap.slice(), anchorX, anchorY)

    override val type = TileType.BRICK


}

inline fun Layer.brick(bitmap: Bitmap, callback: @ViewDslMarker Brick.() -> Unit = {}): Brick {
    return Brick(bitmap).addTo(this, callback)
}
