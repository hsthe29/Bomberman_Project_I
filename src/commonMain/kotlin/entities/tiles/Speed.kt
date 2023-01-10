package entities.tiles

import com.soywiz.korma.annotations.*
import core.base.*
import load.*
import ui.level.*

class Speed(val layer: Layer,
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

inline fun Layer.speed(info: TileInfo, callback: @ViewDslMarker Speed.() -> Unit = {}): Speed {
    return Speed(this, info).apply(callback)
}

