package load

enum class TileType {
    NONE, GROUND, HEALTH, BOMB_INCR, SPEEDUP, ATTACK, FLAME, KEY, GATE, STONE, BRICK
}
data class TileInfo(val url: String,
                    val type: TileType = TileType.NONE,
                    val row: Int, val col: Int)

data class LayerInfo(val layerName: String, val tileInfo: List<TileInfo>)
data class TileMap(val player: Pair<Int, Int>, val enemies: List<Pair<Int, Int>>, val layers: List<LayerInfo>)

