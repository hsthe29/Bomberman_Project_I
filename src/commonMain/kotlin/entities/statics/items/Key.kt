package entities.statics.items

import com.soywiz.korge.view.*
import core.base.*
import entities.statics.*
import load.*
import ui.level.*

class Key(val layer: Layer,
          info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

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

