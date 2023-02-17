package entities.dynamics

import com.soywiz.klock.*
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.interpolation.*
import core.base.*
import core.physics.*
import entities.base.*
import entities.bomb.*
import entities.statics.items.*
import load.*
import ui.level.*
import kotlin.coroutines.*

suspend inline fun World.bomber(callback: @ViewDslMarker Player.() -> Unit = {}): Player {
    val spriteMap = resourcesVfs["player-skin.png"].readBitmap()
    val bomber = Player(this, bomberAnimations(spriteMap)).apply(callback)
    bomber.instance.addTo(this)
    return bomber
}
class Player(world: World, animates: SpriteDirections): Person(world, animates), Bomber {
    override var speed: Double = 1.0
    override var hitPoint: Int = 300
    override var attack: Int = 1

    var maxBomb = 1
    private var bombCount = 0
    override var explosionRadius = 2
    override var type = BombType.EXPLOSION

    override fun dealDamage() = attack

    suspend fun update(input: Input) {
        if(frozen) return
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
        if(input.keys.justPressed(Key.TAB)) {
            if(GameState.nextEntryLevel.first > 0) {
                if (type == BombType.EXPLOSION) {
                    world.bombInfo.first.bitmap = VfsDB.getBitmap("items/frostbomb.png").slice()
                    type = BombType.FROST
                } else {
                    world.bombInfo.first.bitmap = VfsDB.getBitmap("items/bomb.png").slice()
                    type = BombType.EXPLOSION
                }
            }
        }
        if(!anyMovement) {
            stop()
        }
        launch(coroutineContext) { takeItem() }
    }

    override suspend fun putBomb() {
        if(bombCount < maxBomb) {
            val row = (this.y/45.0).toInt()
            val col = (this.x/45.0).toInt()
            if(!world.stoneLayer.occupied(col, row)) {
                bombCount++
                if(type == BombType.EXPLOSION) world.stoneLayer.explosiveBomb(this, col, row) {
                    ticking()
                } else world.stoneLayer.frostBomb(this, col, row) {
                    ticking()
                }
            }
        }
    }

    override fun releaseBomb() { bombCount-- }

    private suspend fun takeItem() {
        val items = world.allTilesWithin(x, y, "item") as List<Item>
        for(item in items) {
            if(distLessThan(x, y, item.x, item.y, 20.0)) {
                VfsDB.getSound("sound/sfx/skeleton_walk.mp3").play().apply { volume = GameState.volume*0.2 }
                item.takeEffect(this)
                break
            }
        }
    }

    override suspend fun takeDamage(damage: Int, freeze: Boolean) {
        if(immune) return
        immune = true
        VfsDB.getSound("sound/sfx/dummy_die.mp3").play().volume = GameState.volume*0.2
        hitPoint -= damage
        if (hitPoint < 1) {
            VfsDB.getSound("sound/sfx/defeat.mp3").play().volume = GameState.volume*0.2
            world.updateHitPoint(0)
            world.notifyGameOver()
        } else world.updateHitPoint(hitPoint)
        if(freeze) {
            immune = false
            freeze()
        }
        else {
            this.instance.colorMul = Colors["#ff7400"]
            this.instance.tween(this.instance::alpha[1.0, 0.4], time = 0.3.seconds, easing = Easing.EASE_IN)
            this.instance.tween(this.instance::alpha[0.4, 1.0], time = 0.3.seconds, easing = Easing.EASE_IN)
            this.instance.tween(this.instance::alpha[1.0, 0.4], time = 0.3.seconds, easing = Easing.EASE_IN)
            this.instance.tween(this.instance::alpha[0.4, 1.0], time = 0.3.seconds, easing = Easing.EASE_IN)
            this.instance.colorMul = Colors["#ffffff"]
            immune = false
        }
    }
}

