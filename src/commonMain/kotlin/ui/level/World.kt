package ui.level

import com.soywiz.korge.view.*
import com.soywiz.korge.view.filter.*
import core.base.*
import entities.tiles.*
import load.*
import ui.*
import java.security.InvalidKeyException
import kotlin.math.max
import kotlin.math.min

fun World.layer(layerName: String, tilePos: List<TileInfo>, callback: @ViewDslMarker Layer.() -> Unit = {})
    = Layer(this, layerName, tilePos).also {
    this.addLayer(it, callback)
}

inline fun Container.world(screen: PlayScreen, callback: @ViewDslMarker World.() -> Unit = {}) =
    World(screen).addTo(this, callback)

class World(val screen: PlayScreen): Container() {
    private val layers = hashMapOf<String, Layer>()
    val putLayer = Layer(this)
    var gate: Gate? = null
        set(value) {
            if(field == null) field = value
        }

    fun addLayer(layer: Layer, callback: @ViewDslMarker Layer.() -> Unit = {}) {
        layers[layer.layerName] = layer
        layer.addTo(this, callback)
        if(layer.layerName == "item") {
            putLayer.addTo(this)
        }

    }

    fun getLayer(name: String): Layer {
        return layers[name]?: throw InvalidKeyException("$name not found")
    }

    // Get tiles around Player
    fun allTilesWithin(x: Double, y: Double, name: String = "stone"): List<OImage> {
        val tiles = mutableListOf<OImage>()
        val layer = layers[name]
        if(layer != null) {
            val stX = max(0, ((x - 55.0)/45.0).toInt())
            val stY = max(0, ((y - 55.0)/45.0).toInt())
            val endX = min(36, ((x + 55.0)/45.0).toInt())
            val endY = min(16, ((y + 55.0)/45.0).toInt())
            for(i in stX..endX) {
                for(j in stY..endY) {
                    if(layer[i, j] != null) {
                        tiles.add(layer[i, j]!!)
                    }
                }
            }
        }
        return tiles
    }

    suspend fun notifyGameOver() {
        this.screen.showGameOver()
    }

    suspend fun notifyWin() {
        this.screen.showGameWin()
    }
}
