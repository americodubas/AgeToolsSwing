package util

import interfaces.Identifiable
import interfaces.Nameable

const val NEW_NAME = "New"

/**
 * Returns a unique name checking the list of used names, follows the pattern `New x`, where `x` is a counter
 */
fun generateName(usedNames: List<Nameable>): String {
    var name = NEW_NAME
    var count = 1
    while (usedNames.find { it.getNewName() == name } != null){
        name = NEW_NAME + count
        count++
    }
    return name
}

/**
 * Returns a unique id checking the list of used ids
 */
fun generateId(usedIds: List<Identifiable>): Int {
    val maxId = usedIds.maxBy { it.getNewId() }
    return when (maxId == null) {
        true -> 1
        false -> maxId!!.getNewId() + 1
    }
}