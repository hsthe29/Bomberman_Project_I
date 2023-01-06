package entities.tiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korio.resources.*
import core.base.*
import load.*
import ui.level.*

class Ground(bitmap: Resourceable<out BaseBmpSlice>,
           anchorX: Double = 0.0,
           anchorY: Double=0.0): OImage(bitmap = bitmap, anchorX = anchorX, anchorY = anchorY) {
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.0,
        anchorY: Double = 0.0,
    ) : this(bitmap.slice(), anchorX, anchorY)

    override val type = TileType.GROUND


}

inline fun Layer.ground(bitmap: Bitmap, callback: @ViewDslMarker Ground.() -> Unit = {}): Ground {
    return Ground(bitmap).addTo(this, callback)
}
