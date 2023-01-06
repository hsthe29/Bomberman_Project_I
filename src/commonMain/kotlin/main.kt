/**
 * @title: Bomberman
 * @version: 1.0.0
 * @author: thehs29
 * @organization: HUST
 *
 * */
import com.google.gson.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korma.geom.*
import load.*
import ui.LoadingScreen
import java.io.*

val gson = Gson()
val gameState = readGameState()
val bitmapDB = BitmapDB// = HashMap<String, Bitmap>()

suspend fun main() = Korge(
        title = gameState.title,
        width = gameState.width,
        height = gameState.height,
        scaleMode = ScaleMode.SHOW_ALL,
    bgcolor = Colors["#5c5c5c"]
        ) {
    File("src/main/resources/").walk().filter { it.path.endsWith("png") }.forEach {
        println(it)
    }
    this.onOut {
        saveGameState(gameState)
    }
    val sceneContainer = sceneContainer()
    sceneContainer.changeTo({ LoadingScreen() })
}
