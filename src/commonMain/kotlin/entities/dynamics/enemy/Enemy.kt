package entities.dynamics.enemy

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korim.color.*
import com.soywiz.korma.interpolation.*
import core.base.*
import entities.base.*
import entities.dynamics.*
import load.*
import ui.level.*

abstract class Enemy(val world: World, animates: SpriteDirections, val kind: MoveKind): Person(animates) {

    protected var direction: MoveDirection

    init {
        direction = if(kind == MoveKind.HORIZONTAL) MoveDirection.LEFT else MoveDirection.DOWN
        play(when(direction) {
            MoveDirection.LEFT -> animates.left
            MoveDirection.DOWN -> animates.down
            else -> { animates.right }
        })
    }
    abstract fun update()

    override suspend fun takeDamage(damage: Int) {
        if(immune) return
        immune = true
        VfsDB.getSound("sound/sfx/dummy_die.mp3").play().volume = GameState.volume*0.2
        hitPoint -= damage
        if (hitPoint < 1) {
            this.alive = false
            this.removeFromParent()
        }
        this.instance.colorMul = Colors["#ff7400"]
        this.instance.tween(this.instance::alpha[1.0, 0.4], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.tween(this.instance::alpha[0.4, 1.0], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.tween(this.instance::alpha[1.0, 0.4], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.tween(this.instance::alpha[0.4, 1.0], time = 0.3.seconds, easing = Easing.EASE_IN)
        this.instance.colorMul = Colors["#ffffff"]
        immune = false
    }
}
