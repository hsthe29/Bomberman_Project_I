package entities.tiles

import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.level.*

class IFlame(val layer: Layer,
             info: TileInfo,
             anchorX: Double = 0.5,
             anchorY: Double=0.5)
    : OImage(bitmap = BitmapDB.getBitmap(info.url), anchorX = anchorX, anchorY = anchorY) {

    override val type = TileType.FLAME

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

}

inline fun Layer.iflame(info: TileInfo, callback: @ViewDslMarker IFlame.() -> Unit = {}): IFlame {
    return IFlame(this, info).apply(callback)
}
