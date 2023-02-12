package entities.statics.tiles

import com.soywiz.korge.view.*
import core.base.*
import entities.statics.*
import load.*
import ui.level.*

class Brick(val layer: Layer,
            info: TileInfo)
    : Tile(bitmap = VfsDB.getBitmap(info.url)) {

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
