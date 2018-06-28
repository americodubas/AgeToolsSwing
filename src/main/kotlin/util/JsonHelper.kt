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

fun writeJsonFile(d: Database, fileName: String) {
    PrintWriter(path + fileName).append(builder.toJson(d)).close()
}

fun writeJsonFile(l: List<Database>, fileName: String) {
    PrintWriter(path + fileName).append(builder.toJson(l)).close()
}

fun readJsonFileToList(fileName: String): List<Database> {
    val bufferedReader: BufferedReader = File(path + fileName).bufferedReader()
    val json = bufferedReader.use { it.readText() }
    return builder.fromJson(json, object : TypeToken<List<Database>>() {}.type)
}

fun readJsonFileToObject(fileName: String): Database {
    val bufferedReader: BufferedReader = File(path + fileName).bufferedReader()
    val json = bufferedReader.use { it.readText() }
    return builder.fromJson(json, Database::class.java)
}