package entities.statics.tiles

import com.soywiz.korge.view.*
import core.base.*
import entities.statics.*
import load.*
import ui.level.*

class Stone(val layer: Layer,
            info: TileInfo,
            anchorX: Double = 0.0,
            anchorY: Double=0.0)
    : Tile(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.STONE
    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col
        y = 45.0*info.row
    }

}

inline fun Layer.stone(info: TileInfo, callback: @ViewDslMarker Stone.() -> Unit = {}): Stone {
    return Stone(this, info).apply(callback)
}
