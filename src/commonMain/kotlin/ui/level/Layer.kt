package ui.level

import com.soywiz.korge.view.*
import entities.statics.*
import entities.statics.items.*
import entities.statics.tiles.*
import load.*

class Layer(var world: World, val layerName:String = "object", tiles: List<TileInfo> = listOf()): Container() {
    private val positions = mutableListOf<Pair<Int, Int>>()
    private val data: Array<Array<Tile?>> = Array(37) { Array(17) { null } }

    init {
        tiles.forEach {
            when(it.type) {
                TileType.HEALTH -> healing(it)
                TileType.KEY -> key(it)
                TileType.BOMB_INCR -> bombIncrease(it)
                TileType.GATE -> gate(it)
                TileType.FLAME -> radiusIncrease(it)
                TileType.ATTACK -> attack(it)
                TileType.BRICK -> brick(it)
                TileType.STONE -> stone(it)
                TileType.GROUND -> ground(it)
                TileType.SPEEDUP -> speedup(it)
                else -> {
                    println("default")
                }
            }
        }
    }

    operator fun get(col: Int, row: Int): Tile? {
        return data[col][row]
    }

    operator fun set(col: Int, row: Int, value: Tile?) {
        if(value != null) {
            value.instance.addTo(this)
            positions.add(Pair(col, row))
        } else {
            data[col][row]?.removeFromParent()
            positions.removeIf { it.first == col && it.second == row}
        }
        data[col][row] = value
    }

    fun allTiles(): List<Tile> {
        return positions.map { this.data[it.first][it.second]!!}
    }

    fun occupied(col: Int, row: Int): Boolean {
        return data[col][row] != null
    }

    fun export(): Array<IntArray> {
        val res = Array(37) { IntArray(17) { 1 } }
        for(p in positions) {
            res[p.first][p.second] = 0
        }
        return res
    }
}
