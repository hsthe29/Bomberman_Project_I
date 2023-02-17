package entities.base

import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*

abstract class AnimationObj(animation: SpriteAnimation): GameObject() {
    private val sprite = Sprite(animation)
    override val instance: BaseImage = sprite
    val onAnimationCompleted: Signal<SpriteAnimation>
        get() = sprite.onAnimationCompleted
    fun play(animation: SpriteAnimation,
             spriteDisplayTime: TimeSpan = animation.defaultTimePerFrame,
             startFrame: Int = -1,
             endFrame: Int = 0,
             reversed: Boolean = false) {
        sprite.playAnimation(animation, spriteDisplayTime, startFrame, endFrame, reversed)
    }

    fun stop() {
        sprite.stopAnimation()
    }
}
