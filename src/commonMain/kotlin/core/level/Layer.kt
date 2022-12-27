package core.level

import bitmaps
import com.soywiz.klogger.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import core.entities.*
import core.tiles.*
import holder

enum class TileType {
    DEFAULT, HEALTH, BOMB_INCR, SPEEDUP, ATTACK, BLAST, KEY, DOOR, STONE, BRICK
}

enum class LayerType {
    DEFAULT, BLOCK, ITEM
}

inline fun Layer.block(bitmap: Bitmap, callback: @ViewDslMarker Block.() -> Unit = {}) =
    Block(bitmap).addTo(this, callback)

inline fun Layer.item(bitmap: Bitmap, callback: @ViewDslMarker Item.() -> Unit = {}) =
    Item(bitmap).addTo(this, callback)

class Layer(val layerName:String, val layerType: LayerType, tiles: List<TileInfo>): Container() {
    val tiles: MutableList<Tile> = mutableListOf()
    val bitMask = Array(37){ Array<Tile?>(17){ null } }

    init {
        when(layerType) {
            LayerType.BLOCK -> {
                var id = 0
                var lastCorY = 90.0
                for(tile in tiles) {
                    if(tile.y > lastCorY && layerName == "stone") {
                        addChild(holder[id++])
                        lastCorY = tile.y
                        Console.log(lastCorY, id)
                    }
                    block(bitmaps[tile.tileUrl]!!).position(tile.x+tile.off_x, tile.y+tile.off_y)
                        .anchor(tile.a_x, tile.a_y).also { this.saveTile(it, tile.x, tile.y) }.apply { isImmortal = tile.tileType != TileType.BRICK }
                }
            }
            LayerType.ITEM -> for(tile in tiles) {
                item(bitmaps[tile.tileUrl]!!).position(tile.x + tile.off_x, tile.y + tile.off_y)
                    .anchor(tile.a_x, tile.a_y).also { this.saveTile(it, tile.x, tile.y) }.apply { this.itemType = tile.tileType }
            }
            else -> {}
        }
    }

    private fun saveTile(tile: Tile,x: Double, y: Double) {
        this.tiles.add(tile)
        val _x = ((x - 10.0)/45.0).toInt()
        val _y = ((y - 10.0)/45.0).toInt()
        bitMask[_x][_y] = tile
    }
}
