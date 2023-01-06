package entities.tiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korio.resources.*
import core.base.*
import load.*
import ui.level.*

class IBomb(bitmap: Resourceable<out BaseBmpSlice>,
           anchorX: Double = 0.5,
           anchorY: Double=0.5): OImage(bitmap = bitmap, anchorX = anchorX, anchorY = anchorY) {
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.5,
        anchorY: Double = 0.5,
    ) : this(bitmap.slice(), anchorX, anchorY)
    override val type = TileType.BOMB_INCR


}

inline fun Layer.ibomb(bitmap: Bitmap, callback: @ViewDslMarker IBomb.() -> Unit = {}): IBomb {
    return IBomb(bitmap).addTo(this, callback)
}
