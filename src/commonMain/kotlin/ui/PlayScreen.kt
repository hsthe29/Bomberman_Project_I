package ui

import bitmapDB
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
import gameState
import load.*
import ui.level.*

class PlayScreen(val isCheckPoint: Boolean, val tileLayer: List<TileLayer>): Scene() {
    private lateinit var world: World
    private lateinit var player: Player
    private lateinit var endGame: Container
    private lateinit var statusbar: Container
    private var receiveKeyInput = true
    private lateinit var hpInfo: Pair<Image, Text>
    private lateinit var bombInfo: Pair<Image, Text>
    private lateinit var flameInfo: Pair<Image, Text>

    override suspend fun SContainer.sceneInit() {

        world = world(this@PlayScreen) {
            y = 40.0
            for(layer in tileLayer) {
                layer(layer.layerName, layer.tileInfo)
            }

            player = player {
                anchor(0.5, 0.65)
                scale(0.775)
                xy(45*3 + 23, 45*8 + 15)
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
                image(bitmapDB.getBitmap("items/heartfull.png")) {
                    anchor(0.5, 0.5)
                    scale = 0.6
                    x = 1000.0 + 20.0
                    y = 30.0
                },
                text("x${player.getHP()}", font = plaguard, color = Colors.BLACK, textSize = 20.0) {
                    x = 1030.0 + 5.0
                    y = 25.0
                })
            bombInfo = Pair(
                image(bitmapDB.getBitmap("items/bomb.png")) {
                    anchor(0.5, 0.5)
                    scale = 0.7
                    x = 1080.0 + 20.0
                    y = 26.0
                },
                text("x${player.getMaxBomb()}", font = plaguard, color = Colors.BLACK, textSize = 20.0) {
                    x = 1110.0 + 5.0
                    y = 25.0
                })
            flameInfo = Pair(
                image(bitmapDB.getBitmap("items/flame.png")) {
                    anchor(0.5, 0.5)
                    scale = 0.8
                    x = 1160.0 + 20.0
                    y = 26.0
                },
                text("x${player.getBlastRange()}", font = plaguard, color = Colors.BLACK, textSize = 20.0) {
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
            uiButton("EXIT") {
                x = 800.0
                y = 600.0
                onClick {
                    sceneContainer.changeTo({ Lobby(gameState.map) })
                }
            }
            uiButton("REPLAY") {
                x = 600.0 - width
                y = 600.0
                onClick {
                    sceneContainer.changeTo({ PlayScreen(isCheckPoint, getTileMap("databases/stage-${gameState.map}_level-${gameState.level}.json").data) })
                }
            }
            show(0.8.seconds, Easing.EASE_IN)
        }
    }

    suspend fun showGameWin() {
        if(isCheckPoint) {
            gameState.map++
            gameState.level = 0
        } else gameState.level++
        receiveKeyInput = false
        world.colorMul = Colors["#7a7a7a"]
        statusbar.colorMul = Colors["#7a7a7a"]
        with(endGame) {
            image(resourcesVfs["items/won_base.png"].readBitmap()) {
                anchor(0.5, 0.5)
                x = 700.0
                y = 400.0
            }
            if(isCheckPoint) {
                uiButton("GO") {
                    x = 700.0 - width*0.5
                    y = 600.0
                    onClick {
                        sceneContainer.changeTo({ Lobby(gameState.map) })
                    }
                }
            }
            else {
                uiButton("EXIT") {
                    x = 800.0
                    y = 600.0
                    onClick {
                        sceneContainer.changeTo({ Lobby(gameState.map) })
                    }
                }
                uiButton("NEXT") {
                    x = 600.0 - width
                    y = 600.0
                    onClick {
                        sceneContainer.changeTo({
                            PlayScreen(
                                gameState.position[gameState.map]!![gameState.level].checkpoint,
                                getTileMap("databases/stage-${gameState.map}_level-${gameState.level}.json").data
                            )
                        })
                    }
                }
            }
            show(0.8.seconds, Easing.EASE_IN)
        }
    }

    fun updateGameState() {

    }
}
