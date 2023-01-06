package load

import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*

object BitmapDB {
    private val data = HashMap<String, Bitmap>()

    suspend fun loadToDB(url: String) {
        data[url] = resourcesVfs[url].readBitmap()
    }

    suspend fun loadAllToDB(files: List<String>) {
        for(url in files) loadToDB(url)
    }

    fun getBitmap(url: String): Bitmap = data[url] ?: TODO("Please make sure that load ${url} first")
    fun clearDB() {
        this.data.clear()
    }
}
