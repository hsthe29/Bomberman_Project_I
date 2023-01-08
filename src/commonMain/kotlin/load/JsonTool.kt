package load

import com.google.gson.*
import java.io.*
import java.lang.reflect.Type

object JsonTool {
    private val gson: Gson = Gson()

    fun <T> loadFromJson(reader: Reader, objType: Type): T {
        return gson.fromJson(reader, objType) as T
    }

    fun <T> loadFromJson(src: String, objType: Type): T {
        val reader = BufferedReader(FileReader(src))
        val res = loadFromJson<T>(reader, objType)
        reader.close()
        return res
    }

    fun saveToJson(writer: Writer, obj: Any) {
        writer.write(gson.toJson(obj))
    }
    fun saveToJson(src: String, obj: Any) {
        val writer = BufferedWriter(FileWriter(src))
        saveToJson(writer, obj)
        writer.close()
    }
}
