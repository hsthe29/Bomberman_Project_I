package entities.statics.items

import com.soywiz.korma.annotations.*
import entities.dynamics.*
import load.*
import ui.level.*

class Speedup(val layer: Layer,
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

    override suspend fun takeEffect(bomber: Player) {
        bomber.speed += 0.1
        bomber.world.itemLayer[col, row] = null
    }
}

inline fun Layer.speedup(info: TileInfo, callback: @ViewDslMarker Speedup.() -> Unit = {}): Speedup {
    return Speedup(this, info).apply(callback)
}

