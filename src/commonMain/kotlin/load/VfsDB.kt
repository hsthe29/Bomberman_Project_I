package load

import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*

object VfsDB {
    private val data = HashMap<String, Bitmap>()
    private val sounds = HashMap<String, Sound>()

    suspend fun loadToDB(url: String) {
        data[url] = resourcesVfs[url].readBitmap()
    }

    suspend fun loadAllToDB(files: Array<String>) {
        for(url in files) loadToDB(url)
    }

    suspend fun loadSound(url: String) {
        sounds[url] = resourcesVfs[url].readSound(AudioDecodingProps.FAST)
    }

    fun getSound(url: String): Sound = sounds[url] ?: TODO("Please make sure that load \"$url\" first")
    fun getBitmap(url: String): Bitmap = data[url] ?: TODO("Please make sure that load \"$url\" first")
    fun clearDB() {
        this.data.clear()
    }
}
