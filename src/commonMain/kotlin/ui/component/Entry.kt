package ui.component

import com.soywiz.korge.view.*
import core.base.*
import load.*

class Entry(val info: EntryInfo)
    : OImage(bitmap = VfsDB.getBitmap(info.entryURL), anchorX = 0.5, anchorY = 0.5) {
    init {
        position(info.x, info.y)
    }

}

inline fun Container.entry(level: Int, info: EntryInfo, callback: @ViewDslMarker Entry.() -> Unit = {}): Entry {
    return Entry(info).addTo(this, callback)
}
