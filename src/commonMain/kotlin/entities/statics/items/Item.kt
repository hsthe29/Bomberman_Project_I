package entities.statics.items

import com.soywiz.korim.bitmap.*
import entities.statics.*

abstract class Item(bitmap: Bitmap): Tile(bitmap) {

    init {
        anchor(0.5, 0.5)
        scale(0.5, 0.5)
    }

}
