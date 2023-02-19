package entities.base

import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korio.async.*

abstract class AnimationObj(animation: SpriteAnimation): GameObject() {
    private var spriteDisplayTime: TimeSpan = 50.milliseconds
    private val sprite = Sprite(animation)
    override val instance: BaseImage = sprite
    val onAnimationCompleted: Signal<SpriteAnimation>
        get() = sprite.onAnimationCompleted
    fun play(animation: SpriteAnimation,
             spriteDisplayTime: TimeSpan = getDefaultTime(animation),
             startFrame: Int = -1,
             endFrame: Int = 0,
             reversed: Boolean = false) {
        sprite.playAnimation(animation, spriteDisplayTime, startFrame, endFrame, reversed)
    }

    fun stop() {
        sprite.stopAnimation()
    }

    private fun getDefaultTime(spriteAnimation: SpriteAnimation?): TimeSpan = when {
        spriteAnimation != null && spriteAnimation.defaultTimePerFrame != TimeSpan.NIL -> spriteAnimation.defaultTimePerFrame
        else -> spriteDisplayTime
    }
}
