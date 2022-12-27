package core.level

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import core.entities.*
import core.tiles.*
import kotlin.math.max
import kotlin.math.min

inline fun World.layer(layerName: String, layerType: LayerType, tilePos: List<TileInfo>, callback: @ViewDslMarker Layer.() -> Unit = {})
    = Layer(layerName, layerType, tilePos).also {
    this.layers[layerName] = it
}.addTo(this, callback)

inline fun World.block(bitmap: Bitmap, callback: @ViewDslMarker Block.() -> Unit = {}) =
    Block(bitmap).addTo(this, callback)

inline fun World.item(bitmap: Bitmap, callback: @ViewDslMarker Item.() -> Unit = {}) =
    Item(bitmap).addTo(this, callback)


inline fun Container.world(callback: @ViewDslMarker World.() -> Unit = {}) =
    World().addTo(this, callback)

class World: Container() {
    val layers = hashMapOf<String, Layer>()

    fun addLayer(layer: Layer) {
        layers[layer.layerName] = layer
    }

    // Get tiles around Player
    fun allTilesWithin(x: Double, y: Double): List<Tile> {
        val tiles = mutableListOf<Tile>()
        val stoneLayer = layers["stone"]
        if(stoneLayer != null) {
            val stX = max(0, ((x - 55.0)/45.0).toInt())
            val stY = max(0, ((y - 55.0)/45.0).toInt())
            val endX = min(36, ((x + 55.0)/45.0).toInt())
            val endY = min(16, ((y + 55.0)/45.0).toInt())
            for(i in stX..endX) {
                for(j in stY..endY) {
                    if(stoneLayer.bitMask[i][j] != null) {
                        tiles.add(stoneLayer.bitMask[i][j]!!)
                    }
                }
            }
        }
        return tiles
    }
}
