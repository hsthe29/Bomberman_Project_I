package entities.statics.items

import com.soywiz.korge.view.*
import entities.dynamics.*
import load.*
import ui.level.*

class HPItem(val layer: Layer,
             info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.HEALTH
    override val row = info.row
    override val col = info.col

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 23.0
        y = 45.0*info.row + 23.0
    }

    override suspend fun takeEffect(bomber: Player) {
        bomber.world.updateHitPoint(++bomber.hitPoint)
        bomber.world.itemLayer[col, row] = null
    }
}

inline fun Layer.hpItem(info: TileInfo, callback: @ViewDslMarker HPItem.() -> Unit = {}): HPItem {
    return HPItem(this, info).apply(callback)
}
