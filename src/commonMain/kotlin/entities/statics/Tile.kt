package entities.statics

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import entities.base.*
import load.*

abstract class Tile(bitmap: Bitmap): GameObject() {
    private val image = Image(bitmap)
    override val instance = image
    abstract val type: TileType
    override fun collideWith(other: GameObject, type: CollisionType): Boolean {
        return true
    }
}
