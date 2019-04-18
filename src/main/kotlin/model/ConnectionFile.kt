package model

import interfaces.Identifiable
import interfaces.Nameable
import java.io.Serializable

data class ConnectionFile(
        var id: Int = 1
        , var name: String = "New"
        , var filepath: String = "path"
        , var userTag: String = "user"
        , var urlTag: String = "url"
        , var passwordTag: String = "password")
    : Serializable, Nameable, Identifiable {

    override fun getNewName() = name

    override fun getNewId() = id

}