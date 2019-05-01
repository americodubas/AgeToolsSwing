package util

import com.google.gson.reflect.TypeToken
import model.Database
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class JsonHelperKtTest {

    private lateinit var database1 : Database
    private lateinit var database2 : Database

    @Before
    fun init() {
        database1 = Database(1,"nameTest1", "userTest1", "urlTest1")
        database2 = Database(2,"nameTest2", "userTest2", "urlTest2")
    }

    @Test
    fun jsonFileShouldExist() {
        val fileName = "test1.json"
        writeJsonFile(database1, fileName)
        assertTrue( File(path + fileName).exists() )
    }

    @Test
    fun jsonFileShouldReturnSameObject() {
        val fileName = "test2.json"
        writeJsonFile(database1, fileName)
        val databaseFromJson = jsonFileToObject<Database>(fileName)
        assertEquals(database1, databaseFromJson)
    }


    @Test
    fun jsonFileShouldReturnSameToList() {
        val fileName = "test3.json"
        val databases = listOf(database1, database2)
        writeJsonFile(databases, fileName)
        val databaseTypeToken = object : TypeToken<List<Database>>() {}
        val databasesFromJson = jsonFileToList(fileName, databaseTypeToken)
        assertEquals(databases, databasesFromJson)
    }
}