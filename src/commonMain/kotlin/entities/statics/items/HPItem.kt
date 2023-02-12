package entities.statics.items

import com.soywiz.korge.view.*
import core.base.*
import entities.statics.*
import load.*
import ui.level.*

class HPItem(val layer: Layer,
             info: TileInfo)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.HEALTH

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 23.0
        y = 45.0*info.row + 23.0
    }

}

inline fun Layer.hpItem(info: TileInfo, callback: @ViewDslMarker HPItem.() -> Unit = {}): HPItem {
    return HPItem(this, info).apply(callback)
}
