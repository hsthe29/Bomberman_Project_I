package entities

import com.soywiz.klock.*
import com.soywiz.korge.view.*
import load.*
import ui.level.*

class Flame(val layer: Layer, col: Int, row: Int): Sprite() {
    private val spriteMap = BitmapDB.getBitmap("items/explosion_red.png")

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

    /* Get location of this component, this method equivalent to View.pos
       but return Pair<Double, Double> instead of IPoint */
    val loc: Pair<Double, Double>
        get() = x to y

    init {
        addTo(layer)
        anchor(0.5, 0.5)
        x = col*45.0 + 22.0
        y = row*45.0 + 22.0

        playAnimation(animation, spriteDisplayTime = 50.milliseconds)
        onAnimationStopped.invoke {
            removeFromParent()
        }
    }


}
