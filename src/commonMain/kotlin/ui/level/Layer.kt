package ui.level

import bitmapDB
import com.soywiz.korge.view.*
import core.base.*
import core.base.OImage as Tile
import entities.tiles.*
import load.*

class Layer(world: World, val layerName:String = "object", tiles: List<TileInfo> = listOf()): Container() {
    private val positions = mutableListOf<Pair<Int, Int>>()
    private val data: Array<Array<Tile?>> = Array(37) { Array(17) { null } }

    init {
        tiles.forEach {
            when(it.type) {
                TileType.HEALTH -> hitpoint(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col + 23.0
                    y = 45.0*it.row + 23.0
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                TileType.KEY -> key(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col + 23.0
                    y = 45.0*it.row + 23.0
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                TileType.BOMB_INCR -> ibomb(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col + 23.0
                    y = 45.0*it.row + 23.0
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                TileType.GATE -> gate(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col + 23.0
                    y = 45.0*it.row + 23.0
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                    world.gate = this
                }
                TileType.FLAME -> iflame(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col + 23.0
                    y = 45.0*it.row + 23.0
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                TileType.BRICK -> brick(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col
                    y = 45.0*it.row
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                TileType.STONE -> stone(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col
                    y = 45.0*it.row
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                TileType.GROUND -> ground(bitmapDB.getBitmap(it.url)) {
                    x = 45.0*it.col
                    y = 45.0*it.row
                    data[it.col][it.row] = this
                    positions.add(Pair(it.col, it.row))
                }
                else -> tile(bitmapDB.getBitmap(it.url))
            }
        }
    }

    operator fun get(col: Int, row: Int): Tile? {
        return data[col][row]
    }

    operator fun set(col: Int, row: Int, value: Tile?) {
        data[col][row] = value
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
