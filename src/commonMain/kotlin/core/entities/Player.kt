package core.entities

import com.soywiz.kds.iterators.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.internal.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Sprite
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import core.*
import core.level.*
import holder

data class PlayerAnimation(
    val spriteAnimationLeft: SpriteAnimation,
    val spriteAnimationRight: SpriteAnimation,
    val spriteAnimationUp: SpriteAnimation,
    val spriteAnimationDown: SpriteAnimation
)

data class KeyAssignment(
    val key: Key,
    val animation: SpriteAnimation,
    val block: (Double) -> Unit
)

suspend inline fun Layer.player(callback: @ViewDslMarker Player.() -> Unit = {}): Player {
    val spriteMap = resourcesVfs["player-skin.png"].readBitmap()
    return Player(this, Player.animations(spriteMap), upKey = Key.W, downKey = Key.S, leftKey = Key.A, rightKey = Key.D)
        .addTo(this, callback)
}

class Player(val layer: Layer,
    val animations: PlayerAnimation,
    upKey: Key, downKey: Key, leftKey: Key, rightKey: Key
) : Sprite(animations.spriteAnimationDown), Collisionable {
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
    override var center = CustomPoint2D(0.0, 0.0)
    override var radius = 5.0
    override var bottomRight = CustomPoint2D(0.0, 0.0)
    override var upperLeft = CustomPoint2D(0.0, 0.0)
    var curentRow = 6
    var prevRow = 6
    private var localY = 375.0

    private val assignments = listOf(
        KeyAssignment(upKey, animations.spriteAnimationUp) {
            y -= it
            if(localY - y >= 45.0) {
                this.curentRow--
                localY -= 45.0
                performSwap()
            }},
        KeyAssignment(downKey, animations.spriteAnimationDown) {
            y += it
            if(y - localY >= 45.0) {
                this.curentRow++
                localY += 45.0
                performSwap()
            }},
        KeyAssignment(leftKey, animations.spriteAnimationLeft) {
//            val tiles = getWorld().allTiles()
//            if(this.canMoveLeft(tiles))
            x -= it },
        KeyAssignment(rightKey, animations.spriteAnimationRight) { x += it },
    )

    @OptIn(KorgeUntested::class)
    private fun performSwap() {
        layer.swapChildren(this, holder[curentRow])
        layer.swapChildren(holder[curentRow], holder[prevRow])
        prevRow = curentRow
    }

    /** Allows to know the appropriate moment to stop the movement animation. */
    /** Allows to know the appropriate moment to stop the movement animation. */
    private var isMoving = false

    val assignedKeyDesc: String
        get() = assignments.map { it.key }.joinToString("/")

    fun handleKeys(inputKeys: InputKeys, disp: Double) {
        // Let's check if any movement keys were pressed during this frame
        val anyMovement: Boolean = assignments // Iterate all registered movement keys
            .filter { inputKeys[it.key] } // Check if this movement key was pressed
            .onEach {
                // If yes, perform its corresponding action and play the corresponding animation
                it.block(disp)
                playAnimation(it.animation)
                com.soywiz.klogger.Console.log(localY, curentRow, y)
            }
            .any()

        if (anyMovement != isMoving) {
            if (isMoving) stopAnimation()
            isMoving = anyMovement
        }
    }

    override fun collidesWith(other: Collisionable): Boolean {
        val Xn = java.lang.Double.max(other.upperLeft.x, java.lang.Double.min(center.x, other.bottomRight.x))
        val Yn = java.lang.Double.max(other.upperLeft.y, java.lang.Double.min(center.y, other.bottomRight.y))
        val Dx = Xn - center.x
        val Dy = Yn - center.y
        return (Dx * Dx + Dy * Dy) <= radius*radius
    }

    override fun collidesWith(otherList: List<Collisionable>): Boolean {
        otherList.fastForEach { if(this.collidesWith(it)) return true }
        return false
    }

    fun update(input: Input) {
        var anyMovement = false
        if (input.keys.pressing(Key.W)) {
            y--
            playAnimation(animations.spriteAnimationUp)
            anyMovement = true
        }
        if (input.keys.pressing(Key.S)) y++
        if (input.keys.pressing(Key.A)) {
            x--
        }
        if (input.keys.pressing(Key.D)) x++
        if(!anyMovement)
            stopAnimation()
    }

    private fun canMoveLeft(nearest: List<Tile>): Boolean {
        val tiles = getWorld().allTilesWithin(x, y)
        for(tile in tiles) {
            if(collidesWith(tile))
                return false
        }
        return true
    }

    fun getWorld(): World {
        return this.parent?.parent as World
    }
}

/*

suspend fun main() = Korge(width = 512, height = 512) {
    Player.hihi()



    /**
     * Extends Sprite with additional state to handle movement/animations
     */
    /**
     * Extends Sprite with additional state to handle movement/animations
     */



    val player1 = PlayerCharacter(spriteAnimationDown, Key.W, Key.S, Key.A, Key.D).apply {
        scale(3.0)
        xy(100, 100)
    }

    text("Player 1 controls: ${player1.assignedKeyDesc}") { position(PADDING, PADDING) }

    val player2 = PlayerCharacter(spriteAnimationDown, Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT).apply {
        scale(3.0)
        xy(300, 100)
    }

    text("Player 2 controls: ${player2.assignedKeyDesc}") {
        positionY(PADDING)
        alignRightToRightOf(parent!!, PADDING)
    }


    addChild(player1)
    addChild(player2)

    addUpdater { time ->
        val scale = 16.milliseconds / time
        val disp = 2 * scale
        val keys = views.input.keys

        player1.handleKeys(keys, disp)
        player2.handleKeys(keys, disp)

        if (keys[Key.L]) { player1.playAnimationLooped(spriteAnimationDown, 100.milliseconds) }
        if (keys[Key.T]) { player1.playAnimation(spriteAnimation = spriteAnimationDown, times = 3, spriteDisplayTime = 200.milliseconds) }
        if (keys[Key.C]) { player1.playAnimationForDuration(1.seconds, spriteAnimationDown); player1.y -= 2 }
        if (keys[Key.ESCAPE]) { player1.stopAnimation() }
    }
    /*onKeyDown {
        when (it.key) {
            Key.LEFT -> {player1.playAnimation(spriteAnimationLeft); player1.x-=2}
            Key.RIGHT ->{player1.playAnimation(spriteAnimationRight); player1.x+=2}
            Key.DOWN -> {player1.playAnimation(spriteAnimationDown); player1.y+=2}
            Key.UP -> {player1.playAnimation(spriteAnimationUp); player1.y-=2}
            Key.A -> {player2.playAnimation(spriteAnimationLeft); player2.x-=2}
            Key.D -> {player2.playAnimation(spriteAnimationRight); player2.x+=2}
            Key.S -> {player2.playAnimation(spriteAnimationDown); player2.y+=2}
            Key.W -> {player2.playAnimation(spriteAnimationUp); player2.y-=2}
            Key.L -> {player1.playAnimationLooped(spriteAnimationDown, 100.milliseconds)}
            Key.T -> {player1.playAnimation(spriteAnimation = spriteAnimationDown, times = 3, spriteDisplayTime = 200.milliseconds)}
            Key.C -> {player1.playAnimationForDuration(1.seconds, spriteAnimationDown); player1.y-=2}
            Key.ESCAPE -> {player1.stopAnimation()}
            else -> {}
        }
    }*/
}

 */
