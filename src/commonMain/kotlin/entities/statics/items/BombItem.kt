package entities.statics.items

import com.soywiz.korge.view.*
import entities.statics.*
import load.*
import ui.level.*

class BombItem(val layer: Layer,
               info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.BOMB_INCR

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }
}

inline fun Layer.bombItem(info: TileInfo, callback: @ViewDslMarker BombItem.() -> Unit = {}): BombItem {
    return BombItem(this, info).apply(callback)
}
