package entities.tiles

import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.level.*

class Brick(val layer: Layer,
            info: TileInfo,
            anchorX: Double = 0.0,
            anchorY: Double=0.0)
    : OImage(bitmap = BitmapDB.getBitmap(info.url), anchorX = anchorX, anchorY = anchorY) {

    override val type = TileType.BRICK

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col
        y = 45.0*info.row
    }

}

inline fun Layer.brick(info: TileInfo, callback: @ViewDslMarker Brick.() -> Unit = {}): Brick {
    return Brick(this, info).apply(callback)
}
