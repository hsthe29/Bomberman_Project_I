package ui

import com.soywiz.klock.*
import com.soywiz.korau.sound.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*
import kotlinx.coroutines.async
import load.*
import ui.component.*

class LoadingScreen : Scene() {
    override suspend fun SContainer.sceneMain() {
        val sound = resourcesVfs["sound/bkg/Intro.mp3"].readMusic()
        val channel = sound.play(PlaybackTimes.INFINITE)
        channel.volume = GameState.volume*0.2
        val background = image(resourcesVfs["backgrounds/loading_bg.png"].readBitmap()) {
            anchor(0.5, 0.5)
            xy(700.0, 400.0)
        }
        val logo = image(resourcesVfs["logo.png"].readBitmap()) {
            anchor(.5, .5)
            scale(0.5)
            position(700, 400)
        }

        tween(logo::rotation[(-10).degrees], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        tween(logo::rotation[10.degrees], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
        tween(logo::rotation[0.degrees], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        tween(logo::y[150.0], logo::scale[0.2], time = 0.7.seconds, easing = Easing.EASE_IN_OUT)

        val progressBar = progressbar(300.0, 20.0, Colors.WHITE, Colors.GREEN) {
            plotOn(x = 700.0, y = 400.0)
        }
        progressBar.assignSlider(async {
            tween(logo::scale[0.2, 0.3], time = 0.4.seconds, easing = Easing.EASE_IN_OUT)
            progressBar.sliding()
        })
        val loader = async{
            GameState.initialize()
            VfsDB.loadAllToDB(urls)
            VfsDB.loadSound("sound/sfx/bomb_explode.mp3")
            VfsDB.loadSound("sound/sfx/bomber_die.mp3")
            VfsDB.loadSound("sound/sfx/bomber_walk.mp3")
            VfsDB.loadSound("sound/sfx/defeat.mp3")
            VfsDB.loadSound("sound/sfx/victory.mp3")
            VfsDB.loadSound("sound/sfx/dummy_die.mp3")
            VfsDB.loadSound("sound/sfx/skeleton_walk.mp3")
            TextFont.load()
        }
        progressBar.assignLoader(loader)
        progressBar.loadAsync()

        val start = image(VfsDB.getBitmap("icons/start-button.png")) {
            anchor(0.5, 0.5)
            scale(0.5)
            position(700, 420)
            hide(0.seconds)
            mouse {
                onOver {
                    this@image.bitmap = VfsDB.getBitmap("icons/start-button-colored.png").slice()
                    this@image.scale = 0.6
                }
                onOut {
                    this@image.bitmap = VfsDB.getBitmap("icons/start-button.png").slice()
                    this@image.scale = 0.5
                }
            }
            onClick {
                channel.stop()
                sceneContainer.changeTo({ Lobby(GameState.nextEntryLevel.first) })
            }
        }
        val settings = image(VfsDB.getBitmap("icons/settings-button.png")) {
            anchor(0.5, 0.5)
            scale(0.5)
            position(700, 510)
            hide(0.seconds)
            mouse {
                onOver {
                    this@image.bitmap = VfsDB.getBitmap("icons/settings-button-colored.png").slice()
                    this@image.scale = 0.6
                }
                onOut {
                    this@image.bitmap = VfsDB.getBitmap("icons/settings-button.png").slice()
                    this@image.scale = 0.5
                }
            }
        }
        val exit = image(VfsDB.getBitmap("icons/exit-button.png")) {
            anchor(0.5, 0.5)
            scale(0.5)
            position(700, 600)
            hide(0.seconds)
            mouse {
                onOver {
                    this@image.bitmap = VfsDB.getBitmap("icons/exit-button-colored.png").slice()
                    this@image.scale = 0.6
                }
                onOut {
                    this@image.bitmap = VfsDB.getBitmap("icons/exit-button.png").slice()
                    this@image.scale = 0.5
                }
            }
            onClick { views.close() }
        }
        settings.onClick {
            val closeDark = resourcesVfs["icons/cancel-dark.png"].readBitmap()
            val closeLight = resourcesVfs["icons/cancel-light.png"].readBitmap()
            val soundIcon = resourcesVfs["icons/sound-icon.png"].readBitmap()
            val volumeUp = resourcesVfs["icons/right-arrow.png"].readBitmap()
            val volumeDown = resourcesVfs["icons/left-arrow.png"].readBitmap()

            container {
                position(700, 450)
                image(resourcesVfs["icons/bright-board.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale(0.6)
                }
                image(closeDark) {
                    anchor(0.5, 0.5)
                    position(250, -200)
                    mouse {
                        onOver {
                            scale = 1.2
                            bitmap = closeLight.slice()
                        }
                        onOut {
                            scale = 1.0
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
                val volume = image(resourcesVfs["icons/volume_${GameState.volume}.png"].readBitmap()) {
                    anchor(0.5, 0.5)
                    scale(0.2)
                    position(0, 20)
                }
                image(volumeDown) {
                    anchor(0.5, 0.5)
                    scale(0.1)
                    position(-160, 20)
                    onClick {
                        if(GameState.volume > 0) {
                            GameState.volume--
                            channel.volume = GameState.volume*0.2
                            volume.bitmap = resourcesVfs["icons/volume_${GameState.volume}.png"].readBitmap().slice()
                        }
                    }
                }
                image(volumeUp) {
                    anchor(0.5, 0.5)
                    scale(0.1)
                    position(160, 20)
                    onClick {
                        if(GameState.volume < 5) {
                            GameState.volume++
                            channel.volume = GameState.volume*0.2
                            volume.bitmap = resourcesVfs["icons/volume_${GameState.volume}.png"].readBitmap().slice()
                        }
                    }
                }
            }
        }
        progressBar.removeFromParent()
        animateParallel {
            tween(logo::y[220.0], logo::scale[0.45], time = 1.seconds, easing = Easing.EASE_IN_OUT)
            start.show(0.5.seconds)
            settings.show(0.5.seconds)
            exit.show(0.5.seconds)
        }
        tween(background::x[700, 800], time = 10.seconds, easing = Easing.EASE_IN_OUT)
        animateParallel {
            sequence(looped = true) {
                tween(background::x[800, 600], time = 20.seconds, easing = Easing.EASE_IN_OUT)
                tween(background::x[600, 800], time = 20.seconds, easing = Easing.EASE_IN_OUT)
            }
        }
    }
}
