package core.entities

import com.soywiz.kds.iterators.*
import com.soywiz.korge.debug.*
import com.soywiz.korge.render.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.*
import com.soywiz.korio.resources.*
import com.soywiz.korma.geom.vector.*
import com.soywiz.korui.*
import core.*
import core.level.*
import java.lang.Double.max
import java.lang.Double.min

abstract class Tile(
    bitmap: Resourceable<out BaseBmpSlice>,
    anchorX: Double = 0.0,
    anchorY: Double = anchorX,
    hitShape: VectorPath? = null,
    smoothing: Boolean = true
) : BaseImage(bitmap, anchorX, anchorY, hitShape, smoothing), ViewFileRef by ViewFileRef.Mixin(), SmoothedBmpSlice, Collisionable {
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.0,
        anchorY: Double = anchorX,
        hitShape: VectorPath? = null,
        smoothing: Boolean = true
    ) : this(bitmap.slice(), anchorX, anchorY, hitShape, smoothing)

    override var center = CustomPoint2D(0.0, 0.0)
    override var radius = 0.0
    override var bottomRight = CustomPoint2D(0.0, 0.0)
    override var upperLeft = CustomPoint2D(0.0, 0.0)

    override fun createInstance(): View = Image(bitmap, anchorX, anchorY, hitShape, smoothing)

    override fun renderInternal(ctx: RenderContext) {
        lazyLoadRenderInternal(ctx, this)
        super.renderInternal(ctx)
    }

    override suspend fun forceLoadSourceFile(views: Views, currentVfs: VfsFile, sourceFile: String?) {
        baseForceLoadSourceFile(views, currentVfs, sourceFile)
        //println("### Trying to load sourceImage=$sourceImage")
        try {
            bitmap = currentVfs["$sourceFile"].readBitmapSlice()
            scale = 1.0
        } catch (e: Throwable) {
            bitmap = Bitmaps.white
            scale = 100.0
        }
    }

    override fun buildDebugComponent(views: Views, container: UiContainer) {
        container.uiCollapsibleSection("Image") {
            uiEditableValue(this@Tile::sourceFile, kind = UiTextEditableValue.Kind.FILE(views.currentVfs) {
                it.extensionLC == "png" || it.extensionLC == "jpg"
            })
        }
        super.buildDebugComponent(views, container)
    }

    /**
    Find the nearest point on the
    rectangle to the center of
    the circle

    Find the distance between the
    nearest point and the center
    of the circle
    Distance between 2 points,
    (x1, y1) & (x2, y2) in
    2D Euclidean space is
    ((x1-x2)**2 + (y1-y2)**2)**0.5

     */

    override fun collidesWith(other: Collisionable): Boolean {
        val Xn = max(other.upperLeft.x, min(center.x, other.bottomRight.x))
        val Yn = max(other.upperLeft.y, min(center.y, other.bottomRight.y))
        val Dx = Xn - center.x
        val Dy = Yn - center.y
        return (Dx * Dx + Dy * Dy) <= radius*radius
    }

    override fun collidesWith(otherList: List<Collisionable>): Boolean {
        otherList.fastForEach { if(this.collidesWith(it)) return true }
        return false
    }
}


class Block(bitmap: Bitmap): Tile(bitmap) {
    var isImmortal = false
}

class Item(bitmap: Bitmap): Tile(bitmap) {
    lateinit var itemType: TileType
}

