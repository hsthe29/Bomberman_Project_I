/**
 * @title: Bomberman
 * @version: 1.0.0
 * @author: thehs29
 * @organization: HUST
 *
 * */

import com.soywiz.korau.sound.*
import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korim.color.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import load.*
import ui.*

suspend fun main() = Korge(
        title = "Bomberman",
        width = 1400,
        height = 800,
        scaleMode = ScaleMode.SHOW_ALL,
    bgcolor = Colors["#5c5c5c"],
    multithreaded = true
        ) {
    val sceneContainer = sceneContainer()
    views.onClose {
        GameState.save()
    }
    sceneContainer.changeTo({ LoadingScreen() })
}
