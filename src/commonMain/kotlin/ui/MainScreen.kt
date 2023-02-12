package ui

import com.soywiz.klock.*
import com.soywiz.korev.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.interpolation.*
import entities.*
import entities.dynamics.*
import load.*
import ui.component.*
import ui.level.*

class MainScreen(val info: EntryInfo): Scene() {
    private lateinit var world: World
    private lateinit var statusbar: Container
    private var receiveKeyInput = true

    private lateinit var pause: Image

    override suspend fun SContainer.sceneInit() {
        val mapInfo = JsonTool.loadFromJson<TileMap>(info.mapURL, TileMap::class.java)
        world = world(this@MainScreen) {
            y = 40.0
            for(layer in mapInfo.layers) {
                layer(layer.layerName, layer.tileInfo)
            }
            bomber = bomber {
                anchor(0.5, 0.5)
                scale(0.775)
                xy(45*mapInfo.player.first + 23, 45*mapInfo.player.second + 23)
            }
            loadText()
        }
        statusbar = container {
            image(resourcesVfs["items/status_bg.png"].readBitmap()) {
                y = -25.0
            }
            roundRect(355, 50, 5, fill = Colors.WHITE) {
                x = 1000.0
                y = 5.0
            }
            world.hpInfo.first.apply {
                    scale = 0.6
                    x = 1000.0 + 20.0
                    y = 30.0
                    addTo(this@container)
                }
            world.hpInfo.second.apply {
                    x = 1030.0 + 5.0
                    y = 25.0
                    addTo(this@container)
                }
            world.bombInfo.first.apply {
                    scale = 0.6
                    x = 1080.0 + 20.0
                    y = 30.0
                    addTo(this@container)
                }
            world.bombInfo.second.apply {
                    x = 1110.0 + 5.0
                    y = 25.0
                    addTo(this@container)
                }
            world.flameInfo.first.apply {
                    scale = 0.7
                    x = 1160.0 + 20.0
                    y = 30.0
                    addTo(this@container)
                }
            world.flameInfo.second.apply {
                    x = 1190.0 + 5.0
                    y = 25.0
                    addTo(this@container)
                }
            world.attackInfo.first.apply {
                    scale = 0.6
                    x = 1240.0 + 20.0
                    y = 30.0
                    addTo(this@container)
                }
            world.attackInfo.second.apply {
                    x = 1270.0 + 5.0
                    y = 25.0
                    addTo(this@container)
                }
            pause = image(resourcesVfs["icons/pause-button.png"].readBitmap(), 0.5, 0.5) {
                scale = 0.4
                x = 1330.0
                y = 30.0
                onClick {
                    if(receiveKeyInput) {
                        bitmap = resourcesVfs["icons/play-button.png"].readBitmapSlice()
                        showGamePaused()
                    }
                }
            }
        }
        world.addUpdater {
            if(receiveKeyInput) {
                launch { bomber.update(input) }
                if (input.keys[Key.LEFT]) if (x < 0.0) x++
                if (input.keys[Key.RIGHT]) if (x > -265.0) x--
            }
        }
    }

    override suspend fun SContainer.sceneMain() {
        animateParallel {
            world.getLayer("item").allTiles().forEach {
                sequence(looped = true) {
                    tween(it.instance::scale[1.0, 0.8], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                    tween(it.instance::scale[0.8, 1.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                }
            }
        }
    }

    private fun preShow() {
        receiveKeyInput = false
        world.colorMul = Colors["#7a7a7a"]
        statusbar.colorMul = Colors["#7a7a7a"]

    }
    private fun endShow() {
        receiveKeyInput = true
        world.colorMul = Colors["#ffffff"]
        statusbar.colorMul = Colors["#ffffff"]

    }

    suspend fun resume() {
        pause.bitmap = resourcesVfs["icons/pause-button.png"].readBitmapSlice()
        endShow()
    }
    suspend fun showGameOver() {
        preShow()
        with(sceneContainer) {
            defeated(this@MainScreen) {
                initialize()
            }
        }
    }

    suspend fun showGameWin() {
        VfsDB.getSound("sound/sfx/victory.mp3").play()
        updateGameState()
        preShow()
        with(sceneContainer) {
            won(this@MainScreen) {
                initialize()
            }
        }
    }

    suspend fun showGamePaused() {
        preShow()
        with(sceneContainer) {
            paused(this@MainScreen) {
                initialize()
            }
        }
    }

    private fun updateGameState() {
        if(info.passed) return
        info.passed = true
        info.entryURL = info.entryURL.replace(".png", "_passed.png")
        GameState.nextEntryLevel = info.nextEntry
    }
}
