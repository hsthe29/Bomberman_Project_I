package entities

import com.soywiz.kds.iterators.*
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Sprite
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import core.base.*
import core.physics.*
import load.*
import ui.level.*

data class PlayerAnimation(
    val spriteAnimationLeft: SpriteAnimation,
    val spriteAnimationRight: SpriteAnimation,
    val spriteAnimationUp: SpriteAnimation,
    val spriteAnimationDown: SpriteAnimation
)

data class Backpack(var hp: Int = 300, var speed: Double = 2.0, var maxBomb: Int = 5, var bombCount: Int = 0, var blastRange: Int = 2, var damage: Int = 1)

suspend inline fun World.player(callback: @ViewDslMarker Player.() -> Unit = {}): Player {
    val spriteMap = resourcesVfs["player-skin.png"].readBitmap()
    return Player(this, Player.animations(spriteMap)/*, upKey = Key.W, downKey = Key.S, leftKey = Key.A, rightKey = Key.D*/)
        .addTo(this, callback)
}

class Player(
    private val world: World,
    private val animations: PlayerAnimation,
) : Sprite(animations.spriteAnimationDown) {
    companion object {
        fun animations(spriteMap: Bitmap): PlayerAnimation = PlayerAnimation(
            spriteAnimationLeft = SpriteAnimation(
                spriteMap = spriteMap,
                spriteWidth = 40,
                spriteHeight = 60,
                marginTop = 0,
                marginLeft = 17*40,
                columns = 6,
                rows = 1
            ),
            spriteAnimationRight = SpriteAnimation(
                spriteMap = spriteMap,
                spriteWidth = 40,
                spriteHeight = 60,
                marginTop = 0,
                marginLeft = 11*40,
                columns = 6,
                rows = 1
            ),
            spriteAnimationUp = SpriteAnimation(
                spriteMap = spriteMap,
                spriteWidth = 40,
                spriteHeight = 60,
                marginTop = 0,
                marginLeft = 0,
                columns = 5,
                rows = 1
            ),
            spriteAnimationDown = SpriteAnimation(
                spriteMap = spriteMap,
                spriteWidth = 40,
                spriteHeight = 60,
                marginTop = 0,
                marginLeft = 5*40,
                columns = 6,
                rows = 1
            )
        )
    }

    /* Get location of this component, this method equivalent to View.pos
       but return Pair<Double, Double> instead of IPoint */
    val loc: Pair<Double, Double>
        get() = x to y

    /** Allows to know the appropriate moment to stop the movement animation. */
    private val backpack = Backpack()

    fun getHP(): Int {
        return backpack.hp
    }

    fun getMaxBomb(): Int {
        return backpack.maxBomb
    }

    fun getBlastRange(): Int {
        return backpack.blastRange
    }

    fun collidesWith(x: Double, y: Double, other: OImage): Boolean {
        val center = Pair(x, y)
        val radius = 14.0
        val bottomRight = Pair(other.x+45.0, other.y+45.0)
        val upperLeft = Pair(other.x, other.y)
        val Xn = java.lang.Double.max(upperLeft.first, java.lang.Double.min(center.first, bottomRight.first))
        val Yn = java.lang.Double.max(upperLeft.second, java.lang.Double.min(center.second, bottomRight.second))
        val Dx = Xn - center.first
        val Dy = Yn - center.second
        return (Dx * Dx + Dy * Dy) <= radius*radius
    }

    fun collidesWith(x: Double, y: Double, otherList: List<OImage>): Boolean {
        otherList.fastForEach { if(this.collidesWith(x, y, it)) return true }
        return false
    }

    fun releaseBomb() {
        backpack.bombCount--
    }

    suspend fun decreaseHP() {
        backpack.hp -= backpack.damage
        if(backpack.hp < 1) {
            world.screen.setHP(0)
            world.notifyGameOver()
        } else world.screen.setHP(backpack.hp)
    }

    suspend fun putBomb() {
        if(backpack.bombCount < backpack.maxBomb) {
            val row = (this.y/45.0).toInt()
            val col = (this.x/45.0).toInt()
            if(!world.putLayer.occupied(col, row)) {
                backpack.bombCount++
                world.putBombAt(this, col, row)
            }
        }
    }

    suspend fun update(input: Input) {
        var anyMovement = false
        val tiles = world.allTilesWithin(x, y)
        val (left, right, up, down) = feasibleDirection(backpack.speed, tiles)
        if (input.keys.pressing(Key.W)) {
            if(up) {
                y -= backpack.speed
                playAnimation(animations.spriteAnimationUp)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.S)) {
            if(down) {
                y += backpack.speed
                playAnimation(animations.spriteAnimationDown)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.A)) {
            if(left) {
                x -= backpack.speed
                playAnimation(animations.spriteAnimationLeft)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.D)) {
            if(right) {
                x += backpack.speed
                playAnimation(animations.spriteAnimationRight)
                anyMovement = true
            }
        }
        if(!anyMovement)
            stopAnimation()

        takeItem()
    }

    private suspend fun takeItem() {
        val items = world.allTilesWithin(x, y, "item")
        for(item in items) {
            if(distLessThan(x, y, item.x, item.y, 20.0)) {
                when(item.type) {
                    TileType.BOMB_INCR -> {
                        backpack.maxBomb++
                        world.screen.setMaxBomb(backpack.maxBomb)
                    }
                    TileType.HEALTH -> {
                        backpack.hp++
                        world.screen.setHP(backpack.hp)
                    }
                    TileType.FLAME -> {
                        backpack.blastRange++
                        world.screen.setBlastRange(backpack.blastRange)
                    }
                    TileType.KEY -> {
                        world.gate?.isOpened = true
                        world.gate?.open()
                    }
                    TileType.GATE -> {
                        if(world.gate!!.isOpened) {
                            world.notifyWin()
                        }
                    }
                    else -> {}
                }
                if(item.type != TileType.GATE)
                    world.getLayer("item").removeTile(col = (item.x/45.0).toInt(), row = (item.y/45.0).toInt())
                break
            }
        }
    }

    /** (left, right, up, down) */
    private fun feasibleDirection(delta: Double, tiles: List<OImage>): BooleanArray {
        val ret = booleanArrayOf(true, true, true, true)
        for(tile in tiles) {
            if(this.collidesWith(x, y-delta, tile))
                ret[2] = false
            if(this.collidesWith(x, y+delta, tile))
                ret[3] = false
            if(this.collidesWith(x-delta, y, tile))
                ret[0] = false
            if(this.collidesWith(x+delta, y, tile))
                ret[1] = false
        }
        return ret
    }
}
