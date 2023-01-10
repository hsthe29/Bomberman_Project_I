package ui.component

import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import core.base.*
import load.*
import ui.*

class Entry(private val game: Lobby, val info: EntryInfo)
    : OImage(bitmap = BitmapDB.getBitmap(info.entryURL), anchorX = 0.5, anchorY = 0.5) {
    init {
        position(info.x, info.y)
        onClick {
            if (info.passed || Pair(info.map, info.level) == GameState.nextEntryLevel) {
                game.sceneContainer.changeTo({
                    PlayScreen(info)
                })
            } else { game.notifyMessage("You have to complete previous level first!") }
        }
    }

}

inline fun Container.entry(game: Lobby, level: Int, info: EntryInfo, callback: @ViewDslMarker Entry.() -> Unit = {}): Entry {
    return Entry(game, info).addTo(this, callback)
}
