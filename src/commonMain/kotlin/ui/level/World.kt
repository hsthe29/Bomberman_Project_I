package ui.level

import com.soywiz.klock.*
import com.soywiz.korge.component.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.interpolation.*
import core.base.*
import entities.*
import entities.bomb.*
import entities.dynamics.*
import entities.dynamics.enemy.*
import entities.statics.*
import entities.statics.items.Gate
import kotlinx.coroutines.*
import load.*
import ui.*
import java.security.InvalidKeyException
import kotlin.coroutines.*
import kotlin.math.max
import kotlin.math.min

fun World.layer(layerName: String, tilePos: List<TileInfo>, callback: @ViewDslMarker Layer.() -> Unit = {})
    = Layer(this, layerName, tilePos).also {
    this.addLayer(it, callback)
}

inline fun Container.world(screen: MainScreen, callback: @ViewDslMarker World.() -> Unit = {}) =
    World(screen).addTo(this, callback)

class World(val screen: MainScreen): Container() {
    private val layers = hashMapOf<String, Layer>()
    val hpInfo: Pair<Image, Text>
    val bombInfo: Pair<Image, Text>
    val flameInfo: Pair<Image, Text>
    val attackInfo: Pair<Image, Text>
    lateinit var bomber: Bomber
    val enemies = arrayListOf<Enemy>()

    val putLayer = Layer(this)
    var gate: Gate? = null
        set(value) {
            if(field == null) field = value
        }

    init {
        hpInfo = Pair(
            Image(VfsDB.getBitmap("items/heartfull.png"), 0.5, 0.5),
            Text("", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0))
        bombInfo = Pair(
            Image(VfsDB.getBitmap("items/bomb.png"), 0.5, 0.5),
            Text("", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0))
        flameInfo = Pair(
            Image(VfsDB.getBitmap("items/flame.png"), 0.5, 0.5),
            Text("", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0))
        attackInfo = Pair(
            Image(VfsDB.getBitmap("items/sword.png"), 0.5, 0.5),
            Text("", font = TextFont.plaguard, color = Colors.BLACK, textSize = 20.0))
    }

    fun loadText() {
        hpInfo.second.text = "x${bomber.hitPoint}"
        bombInfo.second.text = "x${bomber.maxBomb}"
        flameInfo.second.text = "x${bomber.explosionRadius}"
        attackInfo.second.text = "x${bomber.attack}"
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
    fun allTilesWithin(x: Double, y: Double, name: String = "stone"): List<Tile> {
        val tiles = mutableListOf<Tile>()
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

    suspend fun updateHitPoint(value: Int) {
        val img = hpInfo.first
        launch(coroutineContext) {
            img.tween(img::scale[0.6, 0.75], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.75, 0.6], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        hpInfo.second.text = "x$value"
    }

    suspend fun updateBomb(value: Int) {
        val img = bombInfo.first
        launch(coroutineContext) {
            img.tween(img::scale[0.6, 0.75], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.75, 0.6], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        bombInfo.second.text = "x$value"
    }

    suspend fun updateRadius(value: Int) {
        val img = flameInfo.first
        launch(coroutineContext) {
            img.tween(img::scale[0.7, 0.85], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.85, 0.7], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        flameInfo.second.text = "x$value"
    }

    suspend fun updateAttack(value: Int) {
        val img = attackInfo.first
        launch(coroutineContext) {
            img.tween(img::scale[0.6, 0.75], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
            img.tween(img::scale[0.75, 0.6], time = 0.3.seconds, easing = Easing.EASE_IN_OUT)
        }
        attackInfo.second.text = "x$value"
    }

    suspend fun putBombAt(player: Bomber, col: Int, row: Int) {
        putLayer.bomb(col, row) {
            ticking(player)
        }
    }

    suspend fun notifyGameOver() {
        this.screen.showGameOver()
    }

    suspend fun notifyWin() {
        this.screen.showGameWin()
    }
}
