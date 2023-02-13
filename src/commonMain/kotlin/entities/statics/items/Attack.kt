package entities.statics.items

import com.soywiz.korma.annotations.*
import entities.dynamics.*
import load.*
import ui.level.*

class Attack(val layer: Layer,
          info: TileInfo
)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    override val type = TileType.ATTACK

    override val row = info.row
    override val col = info.col

    init {
        layer[info.col, info.row] = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

    override suspend fun takeEffect(bomber: Bomber) {
        bomber.world.updateAttack(++bomber.attack)
        bomber.world.itemLayer[col, row] = null
    }
}

inline fun Layer.attack(info: TileInfo, callback: @ViewDslMarker Attack.() -> Unit = {}): Attack {
    return Attack(this, info).apply(callback)
}
