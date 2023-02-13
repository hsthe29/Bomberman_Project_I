package entities.statics.items

import com.soywiz.korma.annotations.*
import core.base.*
import entities.dynamics.*
import entities.statics.*
import load.*
import ui.level.*

class Speed(val layer: Layer,
             info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.FLAME
    override val row = info.row
    override val col = info.col

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

    override suspend fun takeEffect(bomber: Bomber) {
        bomber.speed += 0.1
        bomber.world.itemLayer[col, row] = null
    }
}

inline fun Layer.speed(info: TileInfo, callback: @ViewDslMarker Speed.() -> Unit = {}): Speed {
    return Speed(this, info).apply(callback)
}

