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
import load.*
import ui.level.*

class PlayScreen(val level: Int, val info: EntryInfo): Scene() {
    private lateinit var world: World
    private lateinit var player: Player
    private lateinit var endGame: Container
    private lateinit var statusbar: Container
    private var receiveKeyInput = true
    private lateinit var hpInfo: Pair<Image, Text>
    private lateinit var bombInfo: Pair<Image, Text>
    private lateinit var flameInfo: Pair<Image, Text>

    override suspend fun SContainer.sceneInit() {
        val layerInfo = JsonTool.loadFromJson<Array<LayerInfo>>(info.mapURL, Array<LayerInfo>::class.java)
        world = world(this@PlayScreen) {
            y = 40.0
            for(layer in layerInfo) {
                layer(layer.layerName, layer.tileInfo)
            }

            player = player {
                anchor(0.5, 0.65)
                scale(0.775)
                xy(45*3 + 22, 45*8 + 15)
            }
        }
        statusbar = container {
            image(resourcesVfs["items/status_bg.png"].readBitmap()) {
                y = -25.0
            }
            roundRect(350, 50, 5, fill = Colors.WHITE) {
                x = 1000.0
                y = 5.0
            }
            hpInfo = Pair(
                image(resourcesVfs["items/heartfull.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale = 0.6
                    x = 1000.0 + 20.0
                    y = 30.0
                },
                text("x${player.getHP()}", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0) {
                    x = 1030.0 + 5.0
                    y = 25.0
                })
            bombInfo = Pair(
                image(resourcesVfs["items/bomb.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale = 0.7
                    x = 1080.0 + 20.0
                    y = 26.0
                },
                text("x${player.getMaxBomb()}", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0) {
                    x = 1110.0 + 5.0
                    y = 25.0
                })
            flameInfo = Pair(
                image(resourcesVfs["items/flame.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale = 0.8
                    x = 1160.0 + 20.0
                    y = 26.0
                },
                text("x${player.getBlastRange()}", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0) {
                    x = 1190.0 + 5.0
                    y = 25.0
                })
        }

        endGame = container {
            hide(time=0.seconds, easing = Easing.EASE_IN_OUT)
        }

        world.addUpdater {
            if(receiveKeyInput) {
                launch { player.update(input) }
                if (input.keys[Key.LEFT]) if (x < 0.0) x++
                if (input.keys[Key.RIGHT]) if (x > -265.0) x--
                if (input.keys.justPressed(Key.SPACE)) {
                    launch { player.putBomb() }
                }
            }
        }
    }

    override suspend fun SContainer.sceneMain() {
        animateParallel {
            world.getLayer("item").allTiles().forEach {
                sequence(looped = true) {
                    tween(it::scale[1.0, 0.8], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                    tween(it::scale[0.8, 1.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                }
            }
        }
    }

    fun setHP(value: Int) {
        val img = hpInfo.first
        launch {
            img.tween(img::scale[0.6, 0.7], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.7, 0.6], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        hpInfo.second.text = "x$value"
    }

    fun setMaxBomb(value: Int) {
        val img = bombInfo.first
        launch {
            img.tween(img::scale[0.7, 0.8], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.8, 0.7], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        bombInfo.second.text = "x$value"
    }

    fun setBlastRange(value: Int) {
        val img = flameInfo.first
        launch {
            img.tween(img::scale[0.8, 0.9], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.9, 0.8], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        flameInfo.second.text = "x$value"
    }
    suspend fun showGameOver() {
        receiveKeyInput = false
        world.colorMul = Colors["#7a7a7a"]
        statusbar.colorMul = Colors["#7a7a7a"]
        with(endGame) {
            image(resourcesVfs["items/defeated_base.png"].readBitmap()) {
                anchor(0.5, 0.5)
                x = 700.0
                y = 400.0
            }
            image(resourcesVfs["icons/exit-game.png"].readBitmap()) {
                anchor(0.5, 0.5)
                scale(0.2)
                x = 900.0 + width*0.5*scale
                y = 600.0
                onClick {
                    sceneContainer.changeTo({ Lobby(GameState.map) })
                }
            }
            image(resourcesVfs["icons/replay-game.png"].readBitmap()) {
                anchor(0.5, 0.5)
                scale(0.2)
                x = 500.0 - width*0.5*scale
                y = 600.0
                onClick {
                    sceneContainer.changeTo({ PlayScreen(level, info) })
                }
            }
            show(0.8.seconds, Easing.EASE_IN)
        }
    }

    suspend fun showGameWin() {
        updateGameState()
        receiveKeyInput = false
        world.colorMul = Colors["#7a7a7a"]
        statusbar.colorMul = Colors["#7a7a7a"]
        with(endGame) {
            image(resourcesVfs["items/won_base.png"].readBitmap()) {
                anchor(0.5, 0.5)
                xy(700.0,400.0)
            }
            image(resourcesVfs["icons/exit-game.png"].readBitmap()) {
                anchor(0.5, 0.5)
                scale(0.2)
                x = 900.0 + width*0.5*scale
                y = 600.0
                onClick {
                    sceneContainer.changeTo({ Lobby(GameState.map) })
                }
            }
            image(resourcesVfs["icons/next-game.png"].readBitmap()) {
                anchor(0.5, 0.5)
                scale(0.2)
                x = 500.0 - width*0.5*scale
                y = 600.0
                onClick {
                    sceneContainer.changeTo({ PlayScreen(level, info) })
                }
            }
            show(0.8.seconds, Easing.EASE_IN)
        }
    }

    private fun updateGameState() {
        info.passed = true
        info.entryURL = info.entryURL.replace(".png", "_passed.png")
        if(info.checkpoint) {
            GameState.map++
            GameState.level = 0
        } else GameState.level++
    }
}
