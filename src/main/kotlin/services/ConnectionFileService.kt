package services

import com.google.gson.reflect.TypeToken
import model.ConnectionFile
import util.*
import java.io.File

const val connectionFileJsonFileName = "connectionFile.json"

/**
 * Creates a new [ConnectionFile],
 * sets the default id and name using the [generateId] and [generateName]
 * then writes it in [connectionFileJsonFileName]
 */
fun createConnectionFile(): ConnectionFile {
    val allConnectionFiles = getAllConnectionFiles()
    val newConnectionFile = ConnectionFile(generateId(allConnectionFiles), generateName(allConnectionFiles))
    writeJsonFile(allConnectionFiles.plus(newConnectionFile), connectionFileJsonFileName)
    return newConnectionFile
}

/**
 * Returns all [ConnectionFile] from [connectionFileJsonFileName]
 */
fun getAllConnectionFiles(): List<ConnectionFile> {
    if ( !File(path + connectionFileJsonFileName).exists() ) {
        createNewFile()
    }
    val connectionFileTypeToken = object : TypeToken<List<ConnectionFile>>() {}
    return jsonFileToList(connectionFileJsonFileName, connectionFileTypeToken)
}

/**
 * Creates the [connectionFileJsonFileName]
 */
private fun createNewFile() {
    writeJsonFile(listOf(ConnectionFile()), connectionFileJsonFileName)
}

/**
 * Returns a [ConnectionFile] by its name
 */
fun getConnectionFileBy(name: String) = getAllConnectionFiles().find { it.name == name }

/**
 * Returns a [ConnectionFile] by its id
 */
fun getConnectionFileBy(id: Int) = getAllConnectionFiles().find { it.id == id }

/**
 * Returns the names of all connection files
 */
fun getAllConnectionFilesNames(): ArrayList<String> {
    val allConnectionFiles = getAllConnectionFiles()
    val names = ArrayList<String>(allConnectionFiles.size)
    allConnectionFiles.forEach {
        names.add(it.name)
    }
    return names
}

/**
 * Update the [ConnectionFile]
 */
fun updateConnectionFile(new: ConnectionFile) {
    val allConnectionFiles = getAllConnectionFiles()
    val old = allConnectionFiles.find { it.id == new.id }
    if (old != null){
        with(old){
            name = new.name
            filepath = new.filepath
            userTag = new.userTag
            urlTag = new.urlTag
        }
    }
    writeJsonFile(allConnectionFiles, connectionFileJsonFileName)
}

/**
 * Delete [ConnectionFile] by its id
 */
fun deleteConnectionFileBy(id: Int) {
    val m = getAllConnectionFiles().toMutableList()
    m.removeAt(m.indexOfFirst { it.id == id })
    writeJsonFile(m, connectionFileJsonFileName)
}

/**
 * Check if [ConnectionFile] name is already used
 */
fun isConnectionFileNameAlreadyUsed(name: String, id: Int) = getAllConnectionFiles().find { it.name == name && it.id != id } != null
