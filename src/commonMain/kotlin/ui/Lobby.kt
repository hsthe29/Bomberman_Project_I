package ui

import com.soywiz.kds.iterators.*
import com.soywiz.klock.*
import com.soywiz.korau.sound.*
import com.soywiz.korge.animate.*
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
import load.*
import ui.component.*

class Lobby(val map: Int): Scene() {
    private lateinit var background: Container
    private lateinit var nextMap: Image

    override suspend fun SContainer.sceneInit() {
        val sound = resourcesVfs["sound/bkg/Forest Imps Inn (forest stage).mp3"].readMusic()
        val channel = sound.play(PlaybackTimes.INFINITE)
        channel.volume = GameState.volume*0.2

        background = container {
            image(resourcesVfs["backgrounds/map_process_${map}.png"].readBitmap()) {
                anchor(.5, .5)
                scale(1.0)
                xy(700.0, 400.0)
            }
            val entries = GameState.entriesOf(map)
            entries.fastForEachWithIndex { id, info ->
                entry(id, info) {
                    scale(0.3)
                    mouse {
                        onOver { scale = 0.35 }
                        onOut { scale = 0.3 }
                    }
                    onClick {
                        if (info.passed || Pair(info.map, info.level) == GameState.nextEntryLevel) {
                            channel.stop()
                            sceneContainer.changeTo({
                                MainScreen(info)
                            })
                        } else {
                            val cn = VfsDB.getSound("sound/sfx/dummy_die.mp3").play(PlaybackTimes.ONE)
                            cn.volume = GameState.volume*0.2
                            notifyMessage("You have to complete previous level first!")
                            cn.stop()
                        }
                    }
                }
            }
            nextMap = image(resourcesVfs["icons/nv-button-${map}.png"].readBitmap()) {
                alpha = 0.7
                anchor(0.5, 0.5)
                scale(0.2)
                position((1 - map * 2) * 650 + 700, 400)
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
                circle(25.0,  fill = Colors.WHITE) {
                    anchor(0.5, 0.5)
                    xy(1360.0, 40.0)
                }
                image(VfsDB.getBitmap("icons/cancel-dark.png")) {
                    anchor(0.5, 0.5)
                    position(1360, 40)
                    mouse {
                        onOver {
                            scale = 1.2
                            bitmap = VfsDB.getBitmap("icons/cancel-light.png").slice()
                        }
                        onOut {
                            scale = 1.0
                            bitmap = VfsDB.getBitmap("icons/cancel-dark.png").slice()
                        }
                    }
                    onClick { views.close() }
                }
            }
        }
        nextMap.onClick {
            launchImmediately {
                background.colorMul = Colors["#7a7a7a"]
                image(resourcesVfs["icons/scene-loading.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale(0.3)
                    position(700, 400)
                }
                if(map == 1)
                    sceneContainer.changeTo({ Lobby(0) })
                else sceneContainer.changeTo({ Lobby(1) })

            }
        }
    }

    override suspend fun SContainer.sceneMain() {

    }

    suspend fun notifyMessage(msg: String, textSize: Double = 35.0, backColor: RGBA = Colors.BLACK, frontColor: RGBA = Colors.WHITE) {
        with(background) {
            val back = text(msg) {
                this.textSize = textSize
                font = TextFont.blomberg
                color = backColor
                x = 700.0 - 0.5 * width-2.0
                y = 50.0 - 0.5 * height+2.0
            }
            val front = text(msg) {
                this.textSize = textSize
                font = TextFont.blomberg
                color = frontColor
                x = 700.0 - 0.5 * width
                y = 50.0 - 0.5 * height
            }
            animateParallel {
                tween(back::y[40.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                tween(front::y[40.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
            }
            animateParallel {
                tween(back::y[40.0, -30.0], time = 0.8.seconds, easing = Easing.EASE_IN_OUT)
                tween(front::y[40.0, -30.0], time = 0.8.seconds, easing = Easing.EASE_IN_OUT)
            }
            back.removeFromParent()
            front.removeFromParent()
        }
    }

    override suspend fun sceneAfterDestroy() {
        super.sceneAfterDestroy()
        println("destroyed")
    }
}
