package load

import gson
import java.io.*
import java.lang.reflect.*

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
