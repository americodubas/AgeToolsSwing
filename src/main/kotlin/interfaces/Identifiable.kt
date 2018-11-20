package interfaces

/**
 * Makes the class identifiable
 * Using getNewId instead of getId because of know kotlin problems with data class getters and setters
 * https://discuss.kotlinlang.org/t/data-class-implement-java-interface-with-some-getters/5458
 */
interface Identifiable {
    fun getNewId(): Int
}