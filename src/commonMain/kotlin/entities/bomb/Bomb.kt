package entities.bomb

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korma.interpolation.*
import entities.base.*
import entities.dynamics.*
import entities.statics.items.*
import load.*
import ui.level.*

abstract class Bomb(
    val layer: Layer,
    val owner: Bomber,
    final override val col: Int, final override val row: Int,
    url: String)
: Item(VfsDB.getBitmap(url)) {
    protected var triggered = false
    var lockMove = false
    val block = arrayOf(false, false, false, false)

    override val type = TileType.NONE

    init{
        layer[col, row] = this
        this.x = col*45.0 + 22.0
        this.y = row*45.0 + 22.0
    }

    override suspend fun takeEffect(player: Player) {
        TODO("Not yet implemented")
    }

    suspend fun ticking() {
        instance.tween(instance::scale[0.7, 0.6], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        instance.tween(instance::scale[0.6, 0.7], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        instance.tween(instance::scale[0.7, 0.6], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        instance.tween(instance::scale[0.6, 0.8], time = 0.85.seconds, easing = Easing.EASE_IN_OUT)
        trigger()
    }

    suspend fun trigger() {
        if(triggered) return
        VfsDB.getSound("sound/sfx/bomb_explode.mp3").play()
        triggered = true
        owner.releaseBomb()
        layer[col, row] = null
        blast()
    }

    protected abstract suspend fun blast()
}
