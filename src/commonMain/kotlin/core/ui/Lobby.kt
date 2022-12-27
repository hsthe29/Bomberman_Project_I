package core.ui

import bitmaps
import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korim.text.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import core.*
import gameState
import kotlin.system.*

class Lobby(private val mapOrder: Int):Scene() {
    private lateinit var background: Container
    private lateinit var nextMap: Image
    override suspend fun SContainer.sceneInit() {
        background = container {
            image(bitmaps["backgrounds/map_process_${mapOrder}.png"]!!) {
                anchor(.5, .5)
                scale(1.0)
                position(700, 400)
            }
            val position = gameState.position[mapOrder]
            val checkpoint = gameState.checkpoint[mapOrder]
            val imageLevel = resourcesVfs["levels/empty_star.png"].readBitmap()
            val imageCheckpoint = resourcesVfs["levels/checkpoint_${mapOrder}.png"].readBitmap()

            position?.forEach {
                image(imageLevel) {
                    anchor(0.5, 0.5)
                    scale(0.2)
                    position(it.x, it.y)
                }
            }

            checkpoint?.let {
                image(imageCheckpoint) {
                    anchor(0.5, 0.5)
                    scale(0.3)
                    position(it.x, it.y)
                    onClick {
                        Console.log("Clicked")
                        sceneContainer.changeTo({ PlayScreen(getTileMap("databases/map-1-level-1.json").data) })
                    }
                }
            }
            nextMap = image(bitmaps["icons/nv-button-${mapOrder}.png"]!!) {
                alpha = 0.7
                anchor(0.5, 0.5)
                scale(0.2)
                position((3 - mapOrder * 2) * 650 + 700, 400)
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
                text(
                    "${gameState.gold}",
                    font = plaguard,
                    textSize = 25.0,
                    color = Colors["#ffa700"],
                    alignment = TextAlignment.MIDDLE_CENTER
                ) {
                    mouse {
                        onOver { scale = 1.1 }
                        onOut { scale = 1.0 }
                    }
                }.xy(60, 30)
                image(bitmaps["icons/cancel-dark.png"]!!) {
                    anchor(0.5, 0.5)
                    scale(0.1)
                    position(250, 30)
                    mouse {
                        onOver {
                            scale = 0.12
                            bitmap = bitmaps["icons/cancel-light.png"]!!.slice()
                        }
                        onOut {
                            scale = 0.1
                            bitmap = bitmaps["icons/cancel-dark.png"]!!.slice()
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
                    sceneContainer.changeTo({ Lobby(2) })
                else sceneContainer.changeTo({ Lobby(1) })

            }
        }
    }

    override suspend fun sceneAfterDestroy() {
        super.sceneAfterDestroy()
        println("destroyed")
    }
}
