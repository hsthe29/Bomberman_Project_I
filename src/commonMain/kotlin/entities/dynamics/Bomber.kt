package entities.dynamics

import com.soywiz.klock.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.interpolation.*
import core.base.*
import core.physics.*
import entities.base.*
import entities.statics.*
import load.*
import ui.level.*
import kotlin.coroutines.*

suspend inline fun World.bomber(callback: @ViewDslMarker Bomber.() -> Unit = {}): Bomber {
    val spriteMap = resourcesVfs["player-skin.png"].readBitmap()
    val bomber = Bomber(this, bomberAnimations(spriteMap)).apply(callback)
    bomber.instance.addTo(this)
    return bomber
}
class Bomber(val world: World, animates: SpriteDirections): Person(animates) {
    override var speed: Double = 1.0
    override var hitPoint: Int = 3
    override var attack: Int = 1
    var maxBomb = 1
    var bombCount = 0
    var explosionRadius = 2

    suspend fun update(input: Input) {
        var anyMovement = false
        val tiles = world.allTilesWithin(x, y)
        val (left, right, up, down) = feasibleDirection(speed, tiles)
        if (input.keys.pressing(Key.W)) {
            if(up) {
                move(MoveDirection.UP)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.S)) {
            if(down) {
                move(MoveDirection.DOWN)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.A)) {
            if(left) {
                move(MoveDirection.LEFT)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.D)) {
            if(right) {
                move(MoveDirection.RIGHT)
                anyMovement = true
            }
        }
        if (input.keys.pressing(Key.SPACE))
            putBomb()
        if(!anyMovement) {
            stop()
        }
        launch(coroutineContext) { takeItem() }
    }
    override fun move(dir: MoveDirection) {
        when(dir) {
            MoveDirection.LEFT -> {
                x -= speed
                play(animates.left)
            }
            MoveDirection.RIGHT -> {
                x += speed
                play(animates.right)
            }
            MoveDirection.UP -> {
                y -= speed
                play(animates.up)
            }
            MoveDirection.DOWN -> {
                y += speed
                play(animates.down)
            }
        }
    }

    suspend fun putBomb() {
        if(bombCount < maxBomb) {
            val row = (this.y/45.0).toInt()
            val col = (this.x/45.0).toInt()
            if(!world.putLayer.occupied(col, row)) {
                bombCount++
                world.putBombAt(this, col, row)
            }
        }
    }

    fun releaseBomb() { bombCount-- }

    private suspend fun takeItem() {
        val items = world.allTilesWithin(x, y, "item")
        for(item in items) {
            if(distLessThan(x, y, item.x, item.y, 20.0)) {
                VfsDB.getSound("sound/sfx/skeleton_walk.mp3").play().apply { volume = GameState.volume*0.2 }
                when(item.type) {
                    TileType.BOMB_INCR -> { world.updateBomb(++maxBomb) }
                    TileType.HEALTH -> { world.updateHitPoint(++hitPoint) }
                    TileType.FLAME -> { world.updateRadius(++explosionRadius) }
                    TileType.ATTACK -> { world.updateAttack(++attack) }
                    TileType.SPEEDUP -> { speed += 0.1 }
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
                    world.getLayer("item")[(item.x/45.0).toInt(), (item.y/45.0).toInt()] = null
                break
            }
        }
    }

    suspend fun decreaseHP(damage: Int) {
        VfsDB.getSound("sound/sfx/dummy_die.mp3").play().volume = GameState.volume*0.2
        hitPoint -= damage
        if (hitPoint < 1) {
            VfsDB.getSound("sound/sfx/defeat.mp3").play().volume = GameState.volume*0.2
            world.updateHitPoint(0)
            world.notifyGameOver()
        } else world.updateHitPoint(hitPoint)
        this.instance.colorMul = Colors["#ff7400"]
        this.instance.tween(this.instance::alpha[1.0, 0.4], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.tween(this.instance::alpha[0.4, 1.0], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.tween(this.instance::alpha[1.0, 0.4], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.tween(this.instance::alpha[0.4, 1.0], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.colorMul = Colors["#ffffff"]
    }
}

