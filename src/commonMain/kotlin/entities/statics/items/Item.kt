package entities.statics.items

import com.soywiz.korim.bitmap.*
import entities.dynamics.*
import entities.statics.*
import load.*

abstract class Item(bitmap: Bitmap): Tile(bitmap) {

    abstract val row: Int
    abstract val col: Int

    init {
        anchor(0.5, 0.5)
        scale(0.5, 0.5)
    }

    abstract suspend fun takeEffect(bomber: Bomber)

}
