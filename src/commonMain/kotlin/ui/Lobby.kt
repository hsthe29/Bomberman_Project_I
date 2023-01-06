package ui

import bitmapDB
import com.soywiz.kds.iterators.*
import com.soywiz.klock.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.interpolation.*
import core.base.*
import gameState
import load.*
import ui.component.*
import kotlin.system.*

class Lobby(private val mapOrder: Int):Scene() {
    private lateinit var background: Container
    private lateinit var nextMap: Image


    init {

    }
    override suspend fun SContainer.sceneInit() {
        background = container {
            image(bitmapDB.getBitmap("backgrounds/map_process_${mapOrder}.png")) {
                anchor(.5, .5)
                scale(1.0)
                position(700, 400)
            }
            val position = gameState.position[mapOrder]!!
            val curLevel = gameState.level

            val games = Array(position.size) {
                val value = position[it]
                entry(it, value.passed, value.checkpoint, resourcesVfs[value.url].readBitmap()) {
                    scale(0.3)
                    position(value.x, value.y)
                    mouse {
                        onOver { scale = 0.35 }
                        onOut { scale = 0.3 }
                    }
                }
            }

            for((i, game) in games.withIndex()) {
                game.onClick {
                    if (gameState.map >= mapOrder && game.level <= curLevel) {
                        println("${gameState.map}, ${mapOrder}")
                        sceneContainer.changeTo({
                            PlayScreen(
                                game.isCheckPoint,
                                getTileMap( "databases/stage-${gameState.map}_level-${gameState.level}.json")
                                    .data)
                        })
                    } else {
                        println("hihi")
                        notifyMessage("You have to complete previous level first!")
                    }
                }

            }
            nextMap = image(bitmapDB.getBitmap("icons/nv-button-${mapOrder}.png")) {
                alpha = 0.7
                anchor(0.5, 0.5)
                scale(0.2)
                position((1 - mapOrder * 2) * 650 + 700, 400)
                mouse {
                    onOver {
                        scale = 0.25
                        alpha = 1.0
                    }
                    onOut {
                        scale = 0.2
                        alpha = 0.7
                    }
                }
            }
            container {
                roundRect(300, 60, 5, fill = Colors.WHITE)
                position(1100, 0)
//                text(
//                    "${gameState.gold}",
//                    font = plaguard,
//                    textSize = 25.0,
//                    color = Colors["#ffa700"],
//                    alignment = TextAlignment.MIDDLE_CENTER
//                ) {
//                    mouse {
//                        onOver { scale = 1.1 }
//                        onOut { scale = 1.0 }
//                    }
//                }.xy(60, 30)
                image(bitmapDB.getBitmap("icons/cancel-dark.png")) {
                    anchor(0.5, 0.5)
                    scale(0.1)
                    position(250, 30)
                    mouse {
                        onOver {
                            scale = 0.12
                            bitmap = bitmapDB.getBitmap("icons/cancel-light.png").slice()
                        }
                        onOut {
                            scale = 0.1
                            bitmap = bitmapDB.getBitmap("icons/cancel-dark.png").slice()
                        }
                    }
                    onClick { exitProcess(0) }
                }
            }
        }

    }

    override suspend fun SContainer.sceneMain() {
        nextMap.onClick {
            launchImmediately {
                background.colorMul = Colors["#7a7a7a"]
                image(resourcesVfs["icons/scene-loading.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale(0.3)
                    position(700, 400)
                }
                if(mapOrder == 1)
                    sceneContainer.changeTo({ Lobby(0) })
                else sceneContainer.changeTo({ Lobby(1) })

            }
        }
    }

    private suspend fun notifyMessage(msg: String) {
        Text(msg).apply {
            addTo(background)
            textSize = 35.0
            font = blomberg
            x = 700.0 - 0.5*width
            tween(this::y[50.0, 40.0], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)
            tween(this::y[40.0, -10.0], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)
            removeFromParent()
        }
    }

    override suspend fun sceneAfterDestroy() {
        super.sceneAfterDestroy()
        println("destroyed")
    }
}
