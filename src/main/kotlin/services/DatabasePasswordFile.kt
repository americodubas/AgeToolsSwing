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
    val databasePasswordFile = allPasswords.find { it.databaseId == databaseId && it.connectionFileId == connectionFileId }
    databasePasswordFile?.password = password
    writeJsonFile(allPasswords, databasePasswordFileName)
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