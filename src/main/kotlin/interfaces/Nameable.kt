package interfaces

/**
 * Makes the class nameable
 * Using getNewName instead of getName because of know kotlin problems with data class getters and setters
 * https://discuss.kotlinlang.org/t/data-class-implement-java-interface-with-some-getters/5458
 */
interface Nameable {
    fun getNewName(): String
}