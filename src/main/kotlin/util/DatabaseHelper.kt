package util

import model.ConnectionFile
import model.Database
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

const val fileName = "database.json"
const val newDatabaseName = "New"

private val files = arrayOf(
        ConnectionFile("c:\\jdsv\\ds\\projetos-ds.xml", "user-name", "connection-url"),
        ConnectionFile("c:\\jdsv\\agesocproc\\ini.xml", "username", "url")
)

fun changeConnectionTo(id: Int) {
    val database = getDatabaseBy(id)!!
    files.forEach {
        val document = getDocument(it.filepath)

        setTag(it.userTag, database.user, document)
        setTag(it.urlTag, database.url, document)

        saveDocument(document, it.filepath)
    }
}

private fun saveDocument(document: Document, filepath: String) {
    val transformer = TransformerFactory.newInstance().newTransformer()
    val source = DOMSource(document)
    val result = StreamResult(File(filepath))

    transformer.transform(source, result)
}

private fun setTag(tagName: String, tagValue: String, document: Document) {
    document.getElementsByTagName(tagName).item(0).textContent = tagValue
}

private fun getDocument(filepath: String) =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(filepath))

fun createDatabase(): Database {
    val allDatabases = getAllDatabases()
    val newDatabase = Database(getNewDatabaseId(allDatabases), getNewDatabaseName(allDatabases))
    writeJsonFile(allDatabases.plus(newDatabase), fileName)
    return newDatabase
}

private fun getNewDatabaseName(allDatabases: List<Database>): String {
    var newName = newDatabaseName
    var count = 1
    while (allDatabases.find { it.name == newName } != null){
        newName = newDatabaseName + count
        count++
    }
    return newName
}

private fun getNewDatabaseId(allDatabases: List<Database>): Int {
    val maxId = allDatabases.maxBy { it.id }
    return when (maxId == null) {
        true -> 1
        false -> maxId!!.id + 1
    }
}

fun getAllDatabases(): List<Database> {
    if ( !File(path + fileName).exists() ) {
        createNewFile()
    }
    return readJsonFileToList(fileName)
}

private fun createNewFile() {
    writeJsonFile(listOf(Database()), fileName)
}

fun getDatabaseBy(name: String) = getAllDatabases().find { it.name == name }

fun getDatabaseBy(id: Int) = getAllDatabases().find { it.id == id }

fun getAllDatabasesNames(): ArrayList<String> {
    val allDatabases = getAllDatabases()
    val names = ArrayList<String>(allDatabases.size)
    allDatabases.forEach {
        names.add(it.name)
    }
    return names
}

fun updateDatabase(new: Database) {
    val allDatabases = getAllDatabases()
    val old = allDatabases.find { it.id == new.id }
    if (old != null){
        old.name = new.name
        old.user = new.user
        old.url = new.url
    }
    writeJsonFile(allDatabases, fileName)
}

fun deleteDatabaseBy(id: Int) {
    val m = getAllDatabases().toMutableList()
    m.removeAt(m.indexOfFirst { it.id == id })
    writeJsonFile(m, fileName)
}

fun isNameAlreadyUsed(name: String, id: Int) = getAllDatabases().find { it.name == name && it.id != id } != null
