package entities

import bitmapDB
import com.soywiz.klock.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import core.level.*
import ui.level.*

class Flame(val world: World, col: Int, row: Int): Sprite() {
    private val spriteMap = bitmapDB.getBitmap("items/explosion_red - Copy.png")

    private val animation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 45,
        spriteHeight = 45,
        marginTop = 0,
        marginLeft = 0,
        columns = 10,
        rows = 2,
        byRows = false
    )

    init {
        addTo(world.putLayer)
        anchor(0.5, 0.5)
        x = col*45.0 + 23.0
        y = row*45.0 + 23.0

        playAnimation(animation, spriteDisplayTime = 50.milliseconds)
        onAnimationStopped.invoke {
            removeFromParent()
        }
    }


}
