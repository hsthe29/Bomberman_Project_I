package core.ui

import bitmaps
import com.soywiz.klock.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.font.*
import com.soywiz.korim.format.*
import com.soywiz.korim.text.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*
import gameState
import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlin.system.*

class LoadingScreen : Scene() {
    private val minDegrees = (-10).degrees
    private val maxDegrees = (+10).degrees
    private lateinit var start: Image
    private lateinit var settings: Image
    private lateinit var exit: Image

    override suspend fun SContainer.sceneMain() {
        val background = image(resourcesVfs["backgrounds/loading_bg.png"].readBitmap()) {
            anchor(0.5, 0.5)
            scale(0.9)
            position(650, 400)
        }
        val image = image(resourcesVfs["logo.png"].readBitmap()) {
            anchor(.5, .5)
            scale(0.5)
            position(700, 400)
        }

        image.tween(image::rotation[minDegrees], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)
        image.tween(image::rotation[maxDegrees], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)
        image.tween(image::rotation[0.degrees], time = 0.25.seconds, easing = Easing.EASE_IN_OUT)
        image.tween(image::y[150.0], image::scale[0.3], time = 1.seconds, easing = Easing.EASE_IN_OUT)

        val progressBar = uiProgressBar(width=300.0, height=20.0) {
            ratio = 0.0
            buttonBackColor = Colors.GREEN
        }.xy(550.0, 400.0)

        tween(image::scale[0.2, 0.3], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)
        tween(image::scale[0.3, 0.2], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)

        val gameState = async{
            // from json files
            bitmaps["backgrounds/map_process_1.png"] = resourcesVfs["backgrounds/map_process_1.png"].readBitmap()
            bitmaps["backgrounds/map_process_2.png"] = resourcesVfs["backgrounds/map_process_2.png"].readBitmap()
            bitmaps["icons/exit-button-colored.png"] = resourcesVfs["icons/exit-button-colored.png"].readBitmap()
            bitmaps["icons/exit-button.png"] = resourcesVfs["icons/exit-button.png"].readBitmap()
            bitmaps["icons/start-button-colored.png"] = resourcesVfs["icons/start-button-colored.png"].readBitmap()
            bitmaps["icons/start-button.png"] = resourcesVfs["icons/start-button.png"].readBitmap()
            bitmaps["icons/settings-button-colored.png"] = resourcesVfs["icons/settings-button-colored.png"].readBitmap()
            bitmaps["icons/settings-button.png"] = resourcesVfs["icons/settings-button.png"].readBitmap()
            bitmaps["icons/nv-button-2.png"] = resourcesVfs["icons/nv-button-2.png"].readBitmap()
            bitmaps["icons/nv-button-1.png"] = resourcesVfs["icons/nv-button-1.png"].readBitmap()
            bitmaps["icons/cancel-dark.png"] = resourcesVfs["icons/cancel-dark.png"].readBitmap()
            bitmaps["icons/cancel-light.png"] = resourcesVfs["icons/cancel-light.png"].readBitmap()

            bitmaps["tiles/dark-floor-1.png"] = resourcesVfs["tiles/dark-floor-1.png"].readBitmap()
            bitmaps["tiles/floor-grass-1.png"] = resourcesVfs["tiles/floor-grass-1.png"].readBitmap()
            bitmaps["tiles/floor-grass-2.png"] = resourcesVfs["tiles/floor-grass-2.png"].readBitmap()
            bitmaps["tiles/floor-grass-3.png"] = resourcesVfs["tiles/floor-grass-3.png"].readBitmap()
            bitmaps["tiles/floor-grass-4.png"] = resourcesVfs["tiles/floor-grass-4.png"].readBitmap()
            bitmaps["tiles/stone-ice-1.png"] = resourcesVfs["tiles/stone-ice-1.png"].readBitmap()
            bitmaps["tiles/stone-ice-2.png"] = resourcesVfs["tiles/stone-ice-2.png"].readBitmap()
            bitmaps["tiles/stone-ice-3.png"] = resourcesVfs["tiles/stone-ice-3.png"].readBitmap()
            bitmaps["tiles/stone-1.png"] = resourcesVfs["tiles/stone-1.png"].readBitmap()
            bitmaps["tiles/stone-2.png"] = resourcesVfs["tiles/stone-2.png"].readBitmap()
            bitmaps["tiles/stone-3.png"] = resourcesVfs["tiles/stone-3.png"].readBitmap()
            bitmaps["items/doorclosed.png"] = resourcesVfs["items/doorclosed.png"].readBitmap()
            bitmaps["items/bombamount.png"] = resourcesVfs["items/bombamount.png"].readBitmap()
            bitmaps["items/key.png"] = resourcesVfs["items/key.png"].readBitmap()
            bitmaps["items/powerup_heart.png"] = resourcesVfs["items/powerup_heart.png"].readBitmap()
            bitmaps["items/powerup_sword.png"] = resourcesVfs["items/powerup_sword.png"].readBitmap()
            bitmaps["items/speedup.png"] = resourcesVfs["items/speedup.png"].readBitmap()
            bitmaps["items/bombpower.png"] = resourcesVfs["items/bombpower.png"].readBitmap()
            bitmaps["items/brick-2.png"] = resourcesVfs["items/brick-2.png"].readBitmap()

            pacifico = resourcesVfs["fonts/Pacifico.ttf"].readTtfFont()
            aller_rg = resourcesVfs["fonts/Aller_rg.ttf"].readTtfFont()
            clear_sans_bold = resourcesVfs["fonts/ClearSans-Bold.ttf"].readTtfFont()
            blomberg = resourcesVfs["fonts/Blomberg.ttf"].readTtfFont()
            plaguard = resourcesVfs["fonts/Plaguard.ttf"].readTtfFont()

            start = image(bitmaps["icons/start-button.png"]!!) {
                anchor(0.5, 0.5)
                scale(0.5)
                position(700, 420)
                hide(0.seconds)
                mouse {
                    onOver {
                        this@image.bitmap = bitmaps["icons/start-button-colored.png"]!!.slice()
                        this@image.scale = 0.6
                    }
                    onOut {
                        this@image.bitmap = bitmaps["icons/start-button.png"]!!.slice()
                        this@image.scale = 0.5
                    }
                }
                onClick {
                    sceneContainer.changeTo({ Lobby(gameState.map) })
                }
            }
            settings = image(bitmaps["icons/settings-button.png"]!!) {
                anchor(0.5, 0.5)
                scale(0.5)
                position(700, 510)
                hide(0.seconds)
                mouse {
                    onOver {
                        this@image.bitmap = bitmaps["icons/settings-button-colored.png"]!!.slice()
                        this@image.scale = 0.6
                    }
                    onOut {
                        this@image.bitmap = bitmaps["icons/settings-button.png"]!!.slice()
                        this@image.scale = 0.5
                    }
                }
            }
            exit = image(bitmaps["icons/exit-button.png"]!!) {
                anchor(0.5, 0.5)
                scale(0.5)
                position(700, 600)
                hide(0.seconds)
                mouse {
                    onOver {
                        this@image.bitmap = bitmaps["icons/exit-button-colored.png"]!!.slice()
                        this@image.scale = 0.6
                    }
                    onOut {
                        this@image.bitmap = bitmaps["icons/exit-button.png"]!!.slice()
                        this@image.scale = 0.5
                    }
                }
                onClick { exitProcess(0) }
            }
            settings.onClick {
                val closeDark = resourcesVfs["icons/cancel-dark.png"].readBitmap()
                val closeLight = resourcesVfs["icons/cancel-light.png"].readBitmap()
                val soundIcon = resourcesVfs["icons/sound-icon.png"].readBitmap()
                val volumeUp = resourcesVfs["icons/right-arrow.png"].readBitmap()
                val volumeDown = resourcesVfs["icons/left-arrow.png"].readBitmap()
                val soundBar = resourcesVfs["icons/empty-sound.png"].readBitmap()
                val soundBar1 = resourcesVfs["icons/one-level-sound.png"].readBitmap()

                container {
                    position(700, 450)
                    image(resourcesVfs["icons/bright-board.png"].readBitmap()) {
                        anchor(0.5, 0.5)
                        scale(0.6)
                    }
                    image(closeDark) {
                        anchor(0.5, 0.5)
                        scale(0.1)
                        position(250, -200)
                        mouse {
                            onOver {
                                scale = 0.12
                                bitmap = closeLight.slice()
                            }
                            onOut {
                                scale = 0.1
                                bitmap = closeDark.slice()
                            }
                        }
                    }.onClick {
                        this.removeChildren()
                        this.removeFromParent()
                    }
                    image(soundIcon) {
                        anchor(0.5, 0.5)
                        scale(0.1)
                        position(-220, 20)
                    }
                    val sb = image(soundBar) {
                        anchor(0.5, 0.5)
                        scale(0.2)
                        position(0, 20)
                    }
                    image(volumeDown) {
                        anchor(0.5, 0.5)
                        scale(0.1)
                        position(-160, 20)
                        onClick {
                            sb.bitmap = soundBar.slice()
                        }
                    }
                    image(volumeUp) {
                        anchor(0.5, 0.5)
                        scale(0.1)
                        position(160, 20)
                        onClick {
                            sb.bitmap = soundBar1.slice()
                        }
                    }
                }
            }
        }

        val loadingProgress = async{
            tween(progressBar::ratio[1.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
        }

        gameState.await(); loadingProgress.await()

        delay(100)
        progressBar.removeFromParent()
        animateParallel {
            tween(image::y[220.0], image::scale[0.45], time = 1.seconds, easing = Easing.EASE_IN_OUT)
            start.show(1.seconds)
            settings.show(1.seconds)
            exit.show(1.seconds)
        }
        animateParallel {
            sequence(looped = true) {
                tween(background::x[650, 750], time = 10.seconds, easing = Easing.EASE_IN_OUT)
                tween(background::x[750, 650], time = 10.seconds, easing = Easing.EASE_IN_OUT)
            }
        }
    }
}
