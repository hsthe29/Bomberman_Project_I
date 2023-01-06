package entities.tiles

import bitmapDB
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korio.resources.*
import core.base.*
import load.*
import ui.level.*

class Gate(bitmap: Resourceable<out BaseBmpSlice>,
           anchorX: Double = 0.5,
           anchorY: Double=0.5): OImage(bitmap = bitmap, anchorX = anchorX, anchorY = anchorY) {
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.5,
        anchorY: Double = 0.5,
    ) : this(bitmap.slice(), anchorX, anchorY)

    var isOpened = false
    override val type = TileType.GATE

    fun open() {
        bitmap = bitmapDB.getBitmap("items/dooropen.png").slice()
    }

}

inline fun Layer.gate(bitmap: Bitmap, callback: @ViewDslMarker Gate.() -> Unit = {}): Gate {
    return Gate(bitmap).addTo(this, callback)
}
