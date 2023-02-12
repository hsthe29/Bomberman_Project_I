package entities.statics.items

import com.soywiz.korma.annotations.*
import core.base.*
import entities.statics.*
import load.*
import ui.level.*

class Speed(val layer: Layer,
             info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

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

