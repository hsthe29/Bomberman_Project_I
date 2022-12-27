package core.ui

import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.animate.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korma.interpolation.*
import core.*
import core.entities.*
import core.level.*
import holder

class PlayScreen(val tileLayer: List<TileLayer>): Scene() {
    private lateinit var world: World
    private lateinit var player: Player

    override suspend fun SContainer.sceneInit() {
        world = world {
            y = 40.0
            for(layer in tileLayer) {
                if (layer.layerType != LayerType.DEFAULT)
                    layer(layer.layerName, layer.layerType, layer.tilePos)
            }
            with(layers["stone"]!!) {
                player = player {
                    anchor(0.5, 0.65)
                    scale(0.775)
                    xy(45*3 + 23, 45*8 + 15)
                }
                swapChildren(holder[6], player)
            }

            player.onClick {
                circle {
                    anchor(0.5, 0.5)
                    position(player.x, player.y)
                }
            }

            addUpdater {
                with(player) {
                    update(input)
                }
                if (input.keys[Key.LEFT]) if (x < 0.0) x++
                if (input.keys.pressing(Key.RIGHT)) if (x > -265.0) x--
                if (input.keys.justPressed(Key.ESCAPE)) views.gameWindow.close(0)
                if (input.keys.justReleased(Key.ENTER)) Console.info("I'm working!")
            }
            onClick {
                this.swapChildren(player, layers["stone"]!!)
            }
        }
    }

    override suspend fun SContainer.sceneMain() {
        animateParallel {
            world.layers["item"]!!.tiles.forEach {
                sequence(looped = true) {
                    tween(it::scale[1.0, 0.8], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                    tween(it::scale[0.8, 1.0], time = 1.seconds, easing = Easing.EASE_IN_OUT)
                }


            }
        }
    }
}
