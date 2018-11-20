package services

import com.google.gson.reflect.TypeToken
import model.ConnectionFile
import util.*
import java.io.File

const val connectionFileJsonFileName = "connectionFile.json"

fun createConnectionFile(): ConnectionFile {
    val allConnectionFiles = getAllConnectionFiles()
    val newConnectionFile = ConnectionFile(generateId(allConnectionFiles), generateName(allConnectionFiles))
    writeJsonFile(allConnectionFiles.plus(newConnectionFile), connectionFileJsonFileName)
    return newConnectionFile
}

fun getAllConnectionFiles(): List<ConnectionFile> {
    if ( !File(path + connectionFileJsonFileName).exists() ) {
        createNewFile()
    }
    val databaseTypeToken = object : TypeToken<List<ConnectionFile>>() {}
    return jsonFileToList(databaseJsonFileName, databaseTypeToken)
}

private fun createNewFile() {
    writeJsonFile(listOf(ConnectionFile()), connectionFileJsonFileName)
}

fun getConnectionFileBy(name: String) = getAllConnectionFiles().find { it.name == name }

fun getConnectionFileBy(id: Int) = getAllConnectionFiles().find { it.id == id }

fun getAllConnectionFilesNames(): ArrayList<String> {
    val allConnectionFiles = getAllConnectionFiles()
    val names = ArrayList<String>(allConnectionFiles.size)
    allConnectionFiles.forEach {
        names.add(it.name)
    }
    return names
}

fun updateConnectionFile(new: ConnectionFile) {
    val allConnectionFiles = getAllConnectionFiles()
    val old = allConnectionFiles.find { it.id == new.id }
    if (old != null){
        old.name = new.name
        old.filepath = new.filepath
        old.userTag = new.userTag
        old.urlTag = new.urlTag
    }
    writeJsonFile(allConnectionFiles, connectionFileJsonFileName)
}

fun deleteConnectionFileBy(id: Int) {
    val m = getAllConnectionFiles().toMutableList()
    m.removeAt(m.indexOfFirst { it.id == id })
    writeJsonFile(m, connectionFileJsonFileName)
}

fun isConnectionFileNameAlreadyUsed(name: String, id: Int) = getAllConnectionFiles().find { it.name == name && it.id != id } != null
