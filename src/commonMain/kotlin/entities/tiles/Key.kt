package entities.tiles

import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.level.*

class Key(val layer: Layer,
          info: TileInfo,
          anchorX: Double = 0.5,
          anchorY: Double=0.5)
    : OImage(bitmap = BitmapDB.getBitmap(info.url), anchorX = anchorX, anchorY = anchorY) {

    override val type = TileType.KEY

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

}

inline fun Layer.key(info: TileInfo, callback: @ViewDslMarker Key.() -> Unit = {}): Key {
    return Key(this, info).apply(callback)
}
