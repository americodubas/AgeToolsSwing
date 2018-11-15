package util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import model.Database
import java.io.BufferedReader
import java.io.File
import java.io.PrintWriter

val builder: Gson = GsonBuilder().setPrettyPrinting().create()
const val path = "C:\\jdsv\\"

fun <T> writeJsonFile(t: T, fileName: String) {
    PrintWriter(path + fileName).append(builder.toJson(t)).close()
}

inline fun <reified T> jsonFileToObject(fileName: String): T {
    val bufferedReader: BufferedReader = File(path + fileName).bufferedReader()
    val json = bufferedReader.use { it.readText() }
    return builder.fromJson(json, T::class.java)
}

inline fun <reified T> jsonFileToList(fileName: String): List<T> {
    val bufferedReader: BufferedReader = File(path + fileName).bufferedReader()
    val json = bufferedReader.use { it.readText() }
    return builder.fromJson(json, object : TypeToken<List<T>>() {}.type)
}