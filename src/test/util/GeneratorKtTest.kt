package util

import model.Database
import org.junit.Test

import org.junit.Assert.*

class GeneratorKtTest {

    @Test
    fun newNameShouldBeNew1() {
        val database = Database()
        val databases = listOf(database)
        assertEquals("${database.name}1", generateName(databases))
    }

    @Test
    fun newNameShouldBeNew2() {
        val database = Database()
        val database1 = Database()
        database1.name = "New1"
        val databases = listOf(database, database1)
        assertEquals("${database.name}2", generateName(databases))
    }

    @Test
    fun newIdShouldBe1() {
        assertEquals(1, generateId(listOf()))
    }

    @Test
    fun newIdShouldBe2() {
        assertEquals(2, generateId(listOf(Database())))
    }
}