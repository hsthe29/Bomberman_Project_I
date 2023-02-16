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

class WonBase(private val scene: MainScreen): Container() {

    suspend fun initialize() {
        hide(time=0.seconds)
        image(resourcesVfs["items/won_base.png"].readBitmap(), 0.5, 0.5) { xy(700.0,400.0) }
        image(resourcesVfs["icons/exit-game.png"].readBitmap(), 0.5, 0.5) {
            scale(0.2)
            xy(900.0 + width*0.5*scale, 600.0)
            onClick {
                scene.sceneContainer.changeTo({
                    this@WonBase.removeFromParent()
                    Lobby(GameState.nextEntryLevel.first)
                })
            }
        }
        image(resourcesVfs["icons/next-game.png"].readBitmap(), 0.5, 0.5) {
            scale(0.2)
            xy(500.0 - width*0.5*scale, 600.0)
            onClick {
                scene.sceneContainer.changeTo({
                    this@WonBase.removeFromParent()
                    MainScreen(GameState.nextEntry(scene.info.nextEntry))
                })
            }
        }
        if(GameState.nextEntryLevel == Pair(1, 0)) {
            image(resourcesVfs["items/earned.png"].readBitmap(), 0.5, 0.5) {
                xy(700.0, 450.0)
            }
        }
        show(0.5.seconds, Easing.EASE_IN)
    }
}

inline fun SceneContainer.won(screen: MainScreen, callback: @ViewDslMarker WonBase.() -> Unit = {}): WonBase {
    return WonBase(screen).addTo(this, callback)
}

