package entities.tiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korio.resources.*
import core.base.*
import load.*
import ui.level.*

class IFlame(bitmap: Resourceable<out BaseBmpSlice>,
           anchorX: Double = 0.5,
           anchorY: Double=0.5): OImage(bitmap = bitmap, anchorX = anchorX, anchorY = anchorY) {
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.5,
        anchorY: Double = 0.5,
    ) : this(bitmap.slice(), anchorX, anchorY)
    var isOpened = false
    override val type = TileType.FLAME


}

inline fun Layer.iflame(bitmap: Bitmap, callback: @ViewDslMarker IFlame.() -> Unit = {}): IFlame {
    return IFlame(bitmap).addTo(this, callback)
}
