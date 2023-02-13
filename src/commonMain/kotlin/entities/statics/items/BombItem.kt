package entities.statics.items

import com.soywiz.korge.view.*
import com.soywiz.korgw.platform.*
import entities.dynamics.*
import entities.statics.*
import load.*
import ui.level.*

class BombItem(val layer: Layer,
               info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.BOMB_INCR

    override val row = info.row
    override val col = info.col

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

    override suspend fun takeEffect(bomber: Bomber) {
        bomber.world.updateBomb(++bomber.maxBomb)
        bomber.world.itemLayer[col, row] = null
    }
}

inline fun Layer.bombItem(info: TileInfo, callback: @ViewDslMarker BombItem.() -> Unit = {}): BombItem {
    return BombItem(this, info).apply(callback)
}
