package ui.component

import com.soywiz.klock.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.annotations.ViewDslMarker
import com.soywiz.korma.interpolation.*
import load.*
import ui.*

class DefeatedBase(private val scene: PlayScreen): Container() {

    suspend fun initialize() {
        hide(time=0.seconds)
        image(resourcesVfs["items/defeated_base.png"].readBitmap(), 0.5, 0.5) { xy(700.0, 400.0) }
        image(resourcesVfs["icons/exit-game.png"].readBitmap(), 0.5, 0.5) {
            scale(0.2)
            xy(900.0 + width*0.5*scale, 600.0)
            onClick { scene.sceneContainer.changeTo({
                this@DefeatedBase.removeFromParent()
                Lobby(GameState.nextEntryLevel.first) })
            }
        }
        image(resourcesVfs["icons/replay-game.png"].readBitmap(), 0.5, 0.5) {
            scale(0.2)
            xy(500.0 - width*0.5*scale, 600.0)
            onClick { scene.sceneContainer.changeTo({
                this@DefeatedBase.removeFromParent()
                PlayScreen(scene.info) })
            }
        }
        show(0.5.seconds, Easing.EASE_IN)
    }
}

inline fun SceneContainer.defeated(screen: PlayScreen, callback: @ViewDslMarker DefeatedBase.() -> Unit = {}): DefeatedBase {
    return DefeatedBase(screen).addTo(this, callback)
}
