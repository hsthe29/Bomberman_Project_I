package load

data class EntryInfo(
    var entryURL: String,
    val mapURL: String,
    val map: Int,
    val level: Int,
    val x: Double,
    val y: Double,
    var passed: Boolean,
    val checkpoint: Boolean,
    val nextEntry: Pair<Int, Int>
)

data class SavedState(var volume: Int,
                      val nextEntryLevel: Pair<Int, Int>,
                      val totalMap: Int,
                      val mapEntries: Array<Array<EntryInfo>>)

object GameState {
    val title: String
    val width: Int
    val height: Int
    var volume: Int
    var nextEntryLevel: Pair<Int, Int>
    val totalMap: Int
    val maxLevels: IntArray
    val mapEntries: Array<Array<EntryInfo>>
    init {
        val temp = JsonTool.loadFromJson<SavedState>("databases/gamestate.json", SavedState::class.java)
        title = "Bomberman"
        width = 1400
        height = 800
        volume = temp.volume
        nextEntryLevel = temp.nextEntryLevel
        totalMap = temp.totalMap
        mapEntries = temp.mapEntries
        maxLevels = IntArray(totalMap){ mapEntries[it].size }
        println("_______________ Load successfully _______________")
    }

    fun initialize() {
        println("Initialized")
    }
    fun entriesOf(map: Int): Array<EntryInfo> = mapEntries[map]

    fun nextEntry(next: Pair<Int, Int>) = mapEntries[next.first][next.second]

    fun save() {
        JsonTool.saveToJson("databases/gamestate.json", SavedState(volume, nextEntryLevel, totalMap, mapEntries))
    }
}


