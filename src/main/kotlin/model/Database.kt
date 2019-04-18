package model

import interfaces.Identifiable
import interfaces.Nameable
import java.io.Serializable

data class Database(
        var id: Int = 1
        , var name: String = "New"
        , var user: String = "User"
        , var url: String = "URL"
        , var databasePasswordFileList: List<DatabasePasswordFile> = emptyList())
    : Serializable, Nameable, Identifiable {

    override fun getNewName() = name

    override fun getNewId() = id

}
