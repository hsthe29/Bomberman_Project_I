package core.base

import com.soywiz.korge.debug.*
import com.soywiz.korge.render.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.*
import com.soywiz.korio.resources.*
import com.soywiz.korma.geom.vector.*
import com.soywiz.korui.*
import load.*
import ui.level.*

open class OImage(
    bitmap: Resourceable<out BaseBmpSlice>,
    anchorX: Double = 0.0,
    anchorY: Double = anchorX,
    hitShape: VectorPath? = null,
    smoothing: Boolean = true,
    type: TileType = TileType.NONE
) : BaseImage(bitmap, anchorX, anchorY, hitShape, smoothing), ViewFileRef by ViewFileRef.Mixin(), SmoothedBmpSlice {

    open val type = type
    constructor(
        bitmap: Bitmap,
        anchorX: Double = 0.0,
        anchorY: Double = anchorX,
        hitShape: VectorPath? = null,
        smoothing: Boolean = true,
        type: TileType = TileType.NONE
    ) : this(bitmap.slice(), anchorX, anchorY, hitShape, smoothing, type)

    /* Get location of this component, this method equivalent to View.pos
       but return Pair<Double, Double> instead of IPoint */
    val loc: Pair<Double, Double>
        get() = x to y

    override fun createInstance(): View = Image(bitmap, anchorX, anchorY, hitShape, smoothing)

    override fun renderInternal(ctx: RenderContext) {
        pos
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
            uiEditableValue(this@OImage::sourceFile, kind = UiTextEditableValue.Kind.FILE(views.currentVfs) {
                it.extensionLC == "png" || it.extensionLC == "jpg"
            })
        }
        super.buildDebugComponent(views, container)
    }

    /**
    - Find the nearest point on the rectangle to the center of the circle
    - Find the distance between the nearest point and the center of the circle
    - Distance between 2 points (x1, y1) and (x2, y2) in 2D Euclidean space is:

        ((x1-x2)**2 + (y1-y2)**2)**0.5

     */


}

inline fun Layer.tile(bitmap: Bitmap, callback: @ViewDslMarker OImage.() -> Unit = {}): OImage {
    return OImage(bitmap).addTo(this, callback)
}
