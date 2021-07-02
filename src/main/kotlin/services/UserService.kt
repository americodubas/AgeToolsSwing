package services

import com.google.gson.reflect.TypeToken
import model.User
import util.*
import java.io.File

const val userJsonFileName = "user.json"

/**
 * Creates a new [User],
 * sets the default id and name using the [generateId] and [generateName]
 * then writes it in [userJsonFileName]
 */
fun createUser(): User {
    val allUsers = getAllUsers()
    val newUser = User(generateId(allUsers), generateName(allUsers))
    writeJsonFile(allUsers.plus(newUser), userJsonFileName)
    return newUser
}

/**
 * Returns all [User] from [userJsonFileName]
 */
fun getAllUsers(): List<User> {
    if ( !File(path + userJsonFileName).exists() ) {
        createNewFile()
    }
    val userTypeToken = object : TypeToken<List<User>>() {}
    return jsonFileToList(userJsonFileName, userTypeToken)
}

/**
 * Creates the [userJsonFileName]
 */
private fun createNewFile() {
    writeJsonFile(listOf(User()), userJsonFileName)
}

/**
 * Returns a [User] by its name and id
 */
fun getUserBy(nameId: String) = getAllUsers().find { it.getNameId() == nameId }

/**
 * Returns a [User] by its id
 */
fun getUserBy(id: Int) = getAllUsers().find { it.id == id }

/**
 * Returns the names of all connection files
 */
fun getAllUsersNames(): ArrayList<String> {
    val allUsers = getAllUsers()
    val names = ArrayList<String>(allUsers.size)
    allUsers.forEach {
        names.add(it.getNameId())
    }
    return names
}

/**
 * Update the [User]
 */
fun updateUser(new: User) {
    val allUsers = getAllUsers()
    val old = allUsers.find { it.id == new.id }
    if (old != null){
        with(old){
            name = new.name
            password = new.password
            description = new.description
            code = new.code
        }
    }
    writeJsonFile(allUsers, userJsonFileName)
}

/**
 * Delete [User] by its id
 */
fun deleteUserBy(id: Int) {
    val m = getAllUsers().toMutableList()
    m.removeAt(m.indexOfFirst { it.id == id })
    writeJsonFile(m, userJsonFileName)
}

/**
 * Check if [User] name is already used
 */
fun isUserNameAlreadyUsed(name: String, id: Int) = getAllUsers().find { it.name == name && it.id != id } != null
