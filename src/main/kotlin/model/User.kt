package model

import interfaces.Identifiable
import interfaces.Nameable
import java.io.Serializable

data class User(
        var id: Int = 1
        , var name: String = "Login"
        , var password: String = "teste123"
        , var code: String = "6161"
        , var description: String = "Lorem ipsum dolor sit.")
    : Serializable, Nameable, Identifiable {

    override fun getNewName() = name

    override fun getNewId() = id

    fun getNameId() = "$id-$name"

}
