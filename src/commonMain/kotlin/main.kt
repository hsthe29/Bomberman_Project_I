/**
 * @title: Bomberman
 * @version: 1.0.0
 * @author: thehs29
 * @organization: HUST
 *
 * */
import com.google.gson.*
import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.Sprite
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import core.*
import core.entities.*
import core.level.*
import core.ui.*

val gson = Gson()
val gameState = readGameState()
val bitmaps = HashMap<String, Bitmap>()
val holder = Array(15) {Circle(radius=0.0)}

suspend fun main() = Korge(
        title = gameState.title,
        width = gameState.width,
        height = gameState.height,
        scaleMode = ScaleMode.SHOW_ALL,
    bgcolor = Colors["#5c5c5c"]
        ) {
    this.onOut {
        saveGameState(gameState)
    }
    val sceneContainer = sceneContainer()
    sceneContainer.changeTo({ LoadingScreen() })
}
