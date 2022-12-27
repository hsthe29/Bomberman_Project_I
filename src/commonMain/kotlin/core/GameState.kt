package core

import core.level.*
import core.tiles.*
import gson
import java.io.*
import java.lang.reflect.Type

data class PosXY(
    val x: Double,
    val y: Double,
    val passed: Boolean
)

data class CustomPoint2D(var x: Double, var y: Double)

data class GameState(
    val title: String,
    val width: Int,
    val height: Int,
    var volume: Int,
    var map: Int,
    var level: Int,
    var gold: Int,
    var hitpoint: Int,
    val position: HashMap<Int, List<PosXY>>,
    val checkpoint: HashMap<Int, PosXY>
)

data class TileLayer(val layerName: String, val layerType: LayerType, val tilePos: List<TileInfo>)
data class TileMap(val data: List<TileLayer>)

fun readGameState(): GameState {
    val br = BufferedReader(FileReader("databases/gamestate.json"))
    val res = gson.fromJson<GameState>(br, GameState::class.java as Type)
    br.close()
    return res
}

fun saveGameState(gameState: GameState) {
    val bw = BufferedWriter(FileWriter("databases/gamestate.json"))
    bw.write(gson.toJson(gameState))
    bw.close()
}

fun getTileMap(url: String): TileMap {
    val br = BufferedReader(FileReader(url))
    val res = gson.fromJson<TileMap>(br, TileMap::class.java as Type)
    br.close()
    return res
}


