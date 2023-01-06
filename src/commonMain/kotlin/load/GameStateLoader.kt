package load

data class TilePos(
    val url: String,
    val x: Double,
    val y: Double,
    val passed: Boolean,
    val checkpoint: Boolean = false
)



data class GameState(
    val title: String,
    val width: Int,
    val height: Int,
    var volume: Int,
    var map: Int,
    var level: Int,
    val position: HashMap<Int, List<TilePos>>,
)


