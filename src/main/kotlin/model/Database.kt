package model

import java.io.Serializable

data class Database(var id: Int = 1, var name: String = "New", var user: String = "User", var url: String = "URL"): Serializable