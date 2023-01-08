package entities.tiles

import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.level.*

class HitPoint(val layer: Layer,
               info: TileInfo,
               anchorX: Double = 0.5,
               anchorY: Double=0.5)
    : OImage(bitmap = BitmapDB.getBitmap(info.url), anchorX = anchorX, anchorY = anchorY) {

    override val type = TileType.HEALTH

    init {
        x = 45.0*info.col + 23.0
        y = 45.0*info.row + 23.0
        layer[info.col, info.row] = this
    }

}

inline fun Layer.hitpoint(info: TileInfo, callback: @ViewDslMarker HitPoint.() -> Unit = {}): HitPoint {
    return HitPoint(this, info).addTo(this, callback)
}
