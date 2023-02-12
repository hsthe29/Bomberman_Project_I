package entities.statics.tiles

import com.soywiz.korge.view.*
import core.base.*
import entities.statics.*
import load.*
import ui.level.*

class Ground(val layer: Layer,
             info: TileInfo)
    : Tile(bitmap = VfsDB.getBitmap(info.url)) {

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
