package ui.component

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korio.resources.*
import core.base.*
import load.*

class Entry(val level: Int, bitmap: Resourceable<out BaseBmpSlice>,
            anchorX: Double = 0.5,
            anchorY: Double=0.5,
            val isCheckPoint: Boolean = false
            ): OImage(bitmap = bitmap, anchorX = anchorX, anchorY = anchorY) {
    constructor(
        level: Int,
        passed: Boolean,
        bitmap: Bitmap,
        isCheckPoint: Boolean = false
    ) : this(level, bitmap.slice(), isCheckPoint = isCheckPoint) {
        this.passed = passed
    }

    var passed = false
}

inline fun Container.entry(level: Int, passed: Boolean, isCheckPoint: Boolean, bitmap: Bitmap, callback: @ViewDslMarker Entry.() -> Unit = {}): Entry {
    return Entry(level, passed, bitmap, isCheckPoint).addTo(this, callback)
}
