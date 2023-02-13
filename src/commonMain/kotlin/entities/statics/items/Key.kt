package entities.statics.items

import com.soywiz.korge.view.*
import core.base.*
import entities.dynamics.*
import entities.statics.*
import load.*
import ui.level.*

class Key(val layer: Layer,
          info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.KEY
    override val row = info.row
    override val col = info.col
    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

    override suspend fun takeEffect(bomber: Bomber) {
        bomber.world.gate.isOpened = true
        bomber.world.gate.open()
        bomber.world.itemLayer[col, row] = null
    }
}

inline fun Layer.key(info: TileInfo, callback: @ViewDslMarker Key.() -> Unit = {}): Key {
    return Key(this, info).apply(callback)
}

