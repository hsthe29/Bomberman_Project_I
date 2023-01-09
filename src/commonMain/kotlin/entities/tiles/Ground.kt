package entities.tiles

import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.level.*

class Ground(val layer: Layer,
             info: TileInfo,
             anchorX: Double = 0.0,
             anchorY: Double=0.0)
    : OImage(bitmap = BitmapDB.getBitmap(info.url),
    anchorX = anchorX,
    anchorY = anchorY) {

    override val type = TileType.GROUND

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col
        y = 45.0*info.row
    }
}

inline fun Layer.ground(info: TileInfo, callback: @ViewDslMarker Ground.() -> Unit = {}): Ground {
    return Ground(this, info).apply(callback)
}
