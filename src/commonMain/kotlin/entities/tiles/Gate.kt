package entities.tiles

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import core.base.*
import load.*
import ui.level.*

class Gate(val layer: Layer, info: TileInfo, anchorX: Double = 0.5,
           anchorY: Double=0.5)
    : OImage(
    bitmap = BitmapDB.getBitmap(info.url),
    anchorX = anchorX,
    anchorY = anchorY) {

    var isOpened = false
    override val type = TileType.GATE

    init {
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
        layer[info.col, info.row] = this
        layer.world.gate = this
    }

    fun open() {
        bitmap = BitmapDB.getBitmap("items/dooropen.png").slice()
    }
}

inline fun Layer.gate(info: TileInfo, callback: @ViewDslMarker Gate.() -> Unit = {}): Gate {
    return Gate(this, info).addTo(this, callback)
}
