package services

import com.google.gson.reflect.TypeToken
import model.ConnectionFile
import model.Database
import org.w3c.dom.Document
import util.*
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

const val databaseJsonFileName = "database.json"

private val files = arrayOf(
        ConnectionFile(1, "ds", "c:\\jdsv\\ds\\projetos-ds.xml", "user-name", "connection-url"),
        ConnectionFile(2, "ini", "c:\\jdsv\\agesocproc\\ini.xml", "username", "url")
)

/**
 * Get a list of all [ConnectionFile] and change then to the chosen [Database]
 */
fun changeConnectionTo(id: Int) {
    val database = getDatabaseBy(id)!!
    val allConnectionFiles = getAllConnectionFiles()

    allConnectionFiles.forEach {
        val document = getDocument(it.filepath)

        setTag(it.userTag, database.user, document)
        setTag(it.urlTag, database.url, document)
        setTag(it.passwordTag, getPassword(database.id, it.id).password, document)

        saveDocument(document, it.filepath)
    }
}

/**
 * Saves changes made in the document
 */
private fun saveDocument(document: Document, filepath: String) {
    val transformer = TransformerFactory.newInstance().newTransformer()
    val source = DOMSource(document)
    val result = StreamResult(File(filepath))

    transformer.transform(source, result)
}

/**
 * Search for the [tagName] in the [document] and change its value to [tagValue]
 */
private fun setTag(tagName: String, tagValue: String, document: Document) {
    document.getElementsByTagName(tagName).item(0).textContent = tagValue
}

/**
 * Returns the document from the [filepath]
 */
private fun getDocument(filepath: String) =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(filepath))

/**
 * Creates a new [Database],
 * sets the default id and name using the [generateId] and [generateName]
 * then writes it in [databaseJsonFileName]
 */
fun createDatabase(): Database {
    val allDatabases = getAllDatabases()
    val newDatabase = Database(generateId(allDatabases), generateName(allDatabases))
    writeJsonFile(allDatabases.plus(newDatabase), databaseJsonFileName)
    return newDatabase
}

/**
 * Returns all [Database] from [databaseJsonFileName]
 */
fun getAllDatabases(): List<Database> {
    if ( !File(path + databaseJsonFileName).exists() ) {
        createNewFile()
    }
    val databaseTypeToken = object : TypeToken<List<Database>>() {}
    return jsonFileToList(databaseJsonFileName, databaseTypeToken)
}

/**
 * Creates the [databaseJsonFileName]
 */
private fun createNewFile() {
    writeJsonFile(listOf(Database()), databaseJsonFileName)
}

/**
 * Returns a [Database] by its name
 */
fun getDatabaseBy(name: String) = getAllDatabases().find { it.name == name }

/**
 * Returns a [Database] by its id
 */
fun getDatabaseBy(id: Int) = getAllDatabases().find { it.id == id }

/**
 * Returns the names of all databases
 */
fun getAllDatabasesNames(): ArrayList<String> {
    val allDatabases = getAllDatabases()
    val names = ArrayList<String>(allDatabases.size)
    allDatabases.forEach {
        names.add(it.name)
    }
    return names
}

/**
 * Update the [Database]
 */
fun updateDatabase(new: Database) {
    val allDatabases = getAllDatabases()
    val old = allDatabases.find { it.id == new.id }
    if (old != null){
        with(old) {
            name = new.name
            user = new.user
            url = new.url
        }
    }
    writeJsonFile(allDatabases, databaseJsonFileName)
}

/**
 * Delete [Database] by its id
 */
fun deleteDatabaseBy(id: Int) {
    val m = getAllDatabases().toMutableList()
    m.removeAt(m.indexOfFirst { it.id == id })
    writeJsonFile(m, databaseJsonFileName)
    deletePasswordByDatabaseId(id)
}

/**
 * Check if [Database] name is already used
 */
fun isDatabaseNameAlreadyUsed(name: String, id: Int) = getAllDatabases().find { it.name == name && it.id != id } != null
