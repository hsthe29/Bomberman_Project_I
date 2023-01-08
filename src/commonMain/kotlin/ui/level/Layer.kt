package ui.level

import com.soywiz.korge.view.*
import core.base.*
import core.base.OImage as Tile
import entities.tiles.*
import load.*

class Layer(var world: World, val layerName:String = "object", tiles: List<TileInfo> = listOf()): Container() {
    private val positions = mutableListOf<Pair<Int, Int>>()
    private val data: Array<Array<Tile?>> = Array(37) { Array(17) { null } }

    init {
        tiles.forEach {
            when(it.type) {
                TileType.HEALTH -> hitpoint(it)
                TileType.KEY -> key(it)
                TileType.BOMB_INCR -> ibomb(it)
                TileType.GATE -> gate(it)
                TileType.FLAME -> iflame(it)
                TileType.BRICK -> brick(it)
                TileType.STONE -> stone(it)
                TileType.GROUND -> ground(it)
                else -> tile(BitmapDB.getBitmap(it.url))
            }
        }
    }

    operator fun get(col: Int, row: Int): Tile? {
        return data[col][row]
    }

    operator fun set(col: Int, row: Int, value: Tile?) {
        data[col][row] = value
        positions.add(Pair(col, row))
    }

    fun allTiles(): List<Tile> {
        return positions.map { this.data[it.first][it.second]!!}
    }

    fun occupied(col: Int, row: Int): Boolean {
        return data[col][row] != null
    }

    fun addTile(col: Int, row: Int, tile: Tile) {
        tile.addTo(this)
        data[col][row] = tile
        positions.add(Pair(col, row))
    }

    fun removeTile(col: Int, row: Int) {
        data[col][row]?.removeFromParent()
        data[col][row] = null
        positions.removeIf { it.first == col && it.second == row}
    }
}
