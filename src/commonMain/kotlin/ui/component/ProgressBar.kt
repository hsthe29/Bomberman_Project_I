package ui.component

import com.soywiz.klock.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korma.interpolation.*
import kotlinx.coroutines.*

class ProgressBar(width: Double, height: Double, baseColor: RGBA, sliderColor: RGBA): Container() {
    private val background = roundRect(width, height, 10.0, 10.0, fill = baseColor)
    private val slider = roundRect(5.0, height, 10.0, 10.0, fill = sliderColor)
    private var loader: Deferred<Unit>? = null
    private var progress: Deferred<Unit>? = null
    init {
        windowBounds.width = width
        windowBounds.height = height
    }



    /** This method plot Progress Bar with anchor = 0.5 */
    fun plotOn(x: Double, y: Double) {
        this.x = x - width*0.5
        this.y = y - height*0.5
    }

    suspend fun sliding() {
        tween(slider::width[background.width*0.95], time=1.2.seconds, easing = Easing.EASE_IN_OUT)
    }
    fun assignSlider(asc: Deferred<Unit>) {
        this.progress = asc
    }

    fun assignLoader(asc: Deferred<Unit>) {
        this.loader = asc
    }

    suspend fun loadAsync() {
        loader?.await(); progress?.await()
        tween(slider::width[background.width*0.95, background.width], time=0.2.seconds, easing = Easing.EASE_IN_OUT)

    }
}

inline fun Container.progressbar(width: Double, height: Double, baseColor: RGBA, sliderColor: RGBA, callback: @ViewDslMarker ProgressBar.() -> Unit = {}): ProgressBar {
    return ProgressBar(width, height, baseColor, sliderColor).addTo(this, callback)
}
