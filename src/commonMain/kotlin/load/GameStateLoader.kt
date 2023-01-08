package load

data class EntryInfo(
    var entryURL: String,
    val mapURL: String,
    val x: Double,
    val y: Double,
    var passed: Boolean,
    val checkpoint: Boolean = false
)

data class SavedState(var volume: Int,
                      var map: Int,
                      var level: Int,
                      val mapInfo: Array<Array<EntryInfo>>)

object GameState{
    val title: String
    val width: Int
    val height: Int
    var volume: Int
    var map: Int
    var level: Int
    val mapInfo: Array<Array<EntryInfo>>
    init {
        val temp = JsonTool.loadFromJson<SavedState>("databases/gamestate.json", SavedState::class.java)
        title = "Bomberman"
        width = 1400
        height = 800
        volume = temp.volume
        map = temp.map
        level = temp.level
        mapInfo = temp.mapInfo
        println("_______________ Load successfully _______________")
    }

    fun initialize() {
        println("Initialized")
    }
    fun mapOf(map: Int): Array<EntryInfo> = mapInfo[map]

    fun save() {
        JsonTool.saveToJson("databases/gamestate.json", SavedState(volume, map, level, mapInfo))
    }
}


