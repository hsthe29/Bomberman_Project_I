package entities.tiles

import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.level.*

class IBomb(val layer: Layer,
            info: TileInfo,
            anchorX: Double = 0.5,
            anchorY: Double=0.5)
    : OImage(bitmap = BitmapDB.getBitmap(info.url), anchorX = anchorX, anchorY = anchorY) {

    override val type = TileType.BOMB_INCR

    init {
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
        layer[info.col, info.row] = this
    }

}

inline fun Layer.ibomb(info: TileInfo, callback: @ViewDslMarker IBomb.() -> Unit = {}): IBomb {
    return IBomb(this, info).addTo(this, callback)
}
