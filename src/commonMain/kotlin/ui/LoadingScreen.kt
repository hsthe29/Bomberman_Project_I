package ui

import bitmapDB
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
            bitmapDB.loadToDB("backgrounds/map_process_0.png")
            bitmapDB.loadToDB("backgrounds/map_process_1.png")
            bitmapDB.loadToDB("icons/exit-button-colored.png")
            bitmapDB.loadToDB("icons/exit-button.png")
            bitmapDB.loadToDB("icons/start-button-colored.png")
            bitmapDB.loadToDB("icons/start-button.png")
            bitmapDB.loadToDB("icons/settings-button-colored.png")
            bitmapDB.loadToDB("icons/settings-button.png")
            bitmapDB.loadToDB("icons/nv-button-1.png")
            bitmapDB.loadToDB("icons/nv-button-0.png")
            bitmapDB.loadToDB("icons/cancel-dark.png")
            bitmapDB.loadToDB("icons/cancel-light.png")

            bitmapDB.loadToDB("tiles/dark-floor-1.png")
            bitmapDB.loadToDB("tiles/floor-grass-1.png")
            bitmapDB.loadToDB("tiles/floor-grass-2.png")
            bitmapDB.loadToDB("tiles/floor-grass-3.png")
            bitmapDB.loadToDB("tiles/floor-grass-4.png")
            bitmapDB.loadToDB("tiles/stone-ice-1.png")
            bitmapDB.loadToDB("tiles/stone-ice-2.png")
            bitmapDB.loadToDB("tiles/stone-ice-3.png")
            bitmapDB.loadToDB("tiles/stone-1.png")
            bitmapDB.loadToDB("tiles/stone-2.png")
            bitmapDB.loadToDB("tiles/stone-3.png")
            bitmapDB.loadToDB("items/doorclosed.png")
            bitmapDB.loadToDB("items/dooropen.png")
            bitmapDB.loadToDB("items/bombamount.png")
            bitmapDB.loadToDB("items/key.png")
            bitmapDB.loadToDB("items/powerup_heart.png")
            bitmapDB.loadToDB("items/powerup_sword.png")
            bitmapDB.loadToDB("items/speedup.png")
            bitmapDB.loadToDB("items/bombpower.png")
            bitmapDB.loadToDB("items/heartfull.png")
            bitmapDB.loadToDB("tiles/brick-2.png")
            bitmapDB.loadToDB("items/bomb.png")
            bitmapDB.loadToDB("items/flame.png")

            bitmapDB.loadToDB("items/icebomb.png")

            bitmapDB.loadToDB("items/explosion_red - Copy.png")

            pacifico = resourcesVfs["fonts/Pacifico.ttf"].readTtfFont()
            aller_rg = resourcesVfs["fonts/Aller_rg.ttf"].readTtfFont()
            clear_sans_bold = resourcesVfs["fonts/ClearSans-Bold.ttf"].readTtfFont()
            blomberg = resourcesVfs["fonts/Blomberg.ttf"].readTtfFont()
            plaguard = resourcesVfs["fonts/Plaguard.ttf"].readTtfFont()

            start = image(bitmapDB.getBitmap("icons/start-button.png")) {
                anchor(0.5, 0.5)
                scale(0.5)
                position(700, 420)
                hide(0.seconds)
                mouse {
                    onOver {
                        this@image.bitmap = bitmapDB.getBitmap("icons/start-button-colored.png").slice()
                        this@image.scale = 0.6
                    }
                    onOut {
                        this@image.bitmap = bitmapDB.getBitmap("icons/start-button.png").slice()
                        this@image.scale = 0.5
                    }
                }
                onClick {
                    sceneContainer.changeTo({ Lobby(gameState.map) })
                }
            }
            settings = image(bitmapDB.getBitmap("icons/settings-button.png")) {
                anchor(0.5, 0.5)
                scale(0.5)
                position(700, 510)
                hide(0.seconds)
                mouse {
                    onOver {
                        this@image.bitmap = bitmapDB.getBitmap("icons/settings-button-colored.png").slice()
                        this@image.scale = 0.6
                    }
                    onOut {
                        this@image.bitmap = bitmapDB.getBitmap("icons/settings-button.png").slice()
                        this@image.scale = 0.5
                    }
                }
            }
            exit = image(bitmapDB.getBitmap("icons/exit-button.png")) {
                anchor(0.5, 0.5)
                scale(0.5)
                position(700, 600)
                hide(0.seconds)
                mouse {
                    onOver {
                        this@image.bitmap = bitmapDB.getBitmap("icons/exit-button-colored.png").slice()
                        this@image.scale = 0.6
                    }
                    onOut {
                        this@image.bitmap = bitmapDB.getBitmap("icons/exit-button.png").slice()
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
            tween(progressBar::ratio[0.9], time = 1.seconds, easing = Easing.EASE_IN_OUT)
        }

        gameState.await(); loadingProgress.await()
        tween(progressBar::ratio[0.9, 1.0], time = 0.5.seconds, easing = Easing.EASE_IN_OUT)
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
