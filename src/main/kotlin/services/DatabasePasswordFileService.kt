package services

import com.google.gson.reflect.TypeToken
import model.DatabasePasswordFile
import util.jsonFileToList
import util.path
import util.writeJsonFile
import java.io.File

const val databasePasswordFileName = "password.json"

/**
 * Returns the [DatabasePasswordFile] from the specific [databaseId] and [connectionFileId].
 * If it does not exist, it will create one with a black password and write it in the json file [databasePasswordFileName].
 */
fun getPassword(databaseId: Int, connectionFileId: Int): DatabasePasswordFile {
    val allPasswords = getAllPasswords()
    var databasePasswordFile = allPasswords.find { it.databaseId == databaseId && it.connectionFileId == connectionFileId }
    if (databasePasswordFile ==  null) {
        databasePasswordFile = DatabasePasswordFile(databaseId, connectionFileId)
        writeJsonFile(allPasswords.plus(databasePasswordFile), databasePasswordFileName)
    }
    return databasePasswordFile
}

/**
 * Update password
 */
fun updatePassword(databaseId: Int, connectionFileId: Int, password: String) {
    val allPasswords = getAllPasswords()
    allPasswords.find { it.databaseId == databaseId && it.connectionFileId == connectionFileId }?.password = password
    writeJsonFile(allPasswords, databasePasswordFileName)
}

/**
 * Delete [DatabasePasswordFile] by database is id
 */
fun deletePasswordByDatabaseId(databaseId: Int) {
    val m = getAllPasswords().toMutableList()
    m.removeAll { it.databaseId == databaseId }
    writeJsonFile(m, databasePasswordFileName)
}

/**
 * Delete [DatabasePasswordFile] by connection file is id
 */
fun deletePasswordByconnectionFileId(connectionFileId: Int) {
    val m = getAllPasswords().toMutableList()
    m.removeAll { it.connectionFileId == connectionFileId }
    writeJsonFile(m, databasePasswordFileName)
}

/**
 * Returns all [DatabasePasswordFile] from [databasePasswordFileName]
 */
fun getAllPasswords(): List<DatabasePasswordFile> {
    if ( !File(path + databasePasswordFileName).exists() ) {
        createNewFile()
    }
    val databaseTypeToken = object : TypeToken<List<DatabasePasswordFile>>() {}
    return jsonFileToList(databasePasswordFileName, databaseTypeToken)
}

/**
 * Creates the [databasePasswordFileName]
 */
private fun createNewFile() {
    writeJsonFile(listOf(DatabasePasswordFile()), databasePasswordFileName)
}