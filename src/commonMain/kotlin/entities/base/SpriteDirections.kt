package entities.base

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import entities.dynamics.*
import load.*

data class SpriteDirections(val left: SpriteAnimation, val right: SpriteAnimation, val up: SpriteAnimation, val down: SpriteAnimation)

fun bomberAnimations(spriteMap: Bitmap) = SpriteDirections(
    left = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 40,
        spriteHeight = 60,
        marginTop = 0,
        marginLeft = 17*40,
        columns = 6,
        rows = 1
    ),
    right = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 40,
        spriteHeight = 60,
        marginTop = 0,
        marginLeft = 11*40,
        columns = 6,
        rows = 1
    ),
    up = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 40,
        spriteHeight = 60,
        marginTop = 0,
        marginLeft = 0,
        columns = 5,
        rows = 1
    ),
    down = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 40,
        spriteHeight = 60,
        marginTop = 0,
        marginLeft = 5*40,
        columns = 6,
        rows = 1
    )
)

fun ghostAnimations(spriteMap: Bitmap) = SpriteDirections(
    left = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 0,
        marginLeft = 0,
        columns = 4,
        rows = 1
    ),
    right = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 25,
        marginLeft = 0,
        columns = 4,
        rows = 1
    ),
    up = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 0,
        marginLeft = 0,
        columns = 4,
        rows = 1
    ),
    down = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 25,
        marginLeft = 0,
        columns = 4,
        rows = 1
    )
)

fun skeletonAnimations(spriteMap: Bitmap) = SpriteDirections(
    left = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 0,
        marginLeft = 0,
        columns = 4,
        rows = 1
    ),
    right = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 25,
        marginLeft = 0,
        columns = 4,
        rows = 1
    ),
    up = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 25,
        marginLeft = 0,
        columns = 4,
        rows = 1
    ),
    down = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 35,
        spriteHeight = 25,
        marginTop = 25,
        marginLeft = 0,
        columns = 4,
        rows = 1
    )
)

fun redExplosionAnimation(spriteMap: Bitmap) = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 45,
        spriteHeight = 45,
        marginTop = 0,
        marginLeft = 0,
        columns = 10,
        rows = 2,
        byRows = false
    )
