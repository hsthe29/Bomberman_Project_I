package entities.statics.items

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import core.base.*
import entities.dynamics.*
import entities.statics.*
import load.*
import ui.level.*

class Gate(val layer: Layer, info: TileInfo, anchorX: Double = 0.5,
           anchorY: Double=0.5)
    : Item(bitmap = VfsDB.getBitmap(info.url)) {

    var isOpened = false

    override val type = TileType.GATE
    override val row = info.row
    override val col = info.col

    init {
        layer[info.col, info.row] = this
        layer.world.gate = this
        x = 45.0*info.col + 22.0
        y = 45.0*info.row + 22.0
    }

    override suspend fun takeEffect(bomber: Bomber) {
        if(bomber.world.gate.isOpened) {
            bomber.world.notifyWin()
        }
    }

    fun open() {
        instance.bitmap = VfsDB.getBitmap("items/dooropen.png").slice()
    }
}

inline fun Layer.gate(info: TileInfo, callback: @ViewDslMarker Gate.() -> Unit = {}): Gate {
    return Gate(this, info).apply(callback)
}
