package model

data class DatabasePasswordFile(
        var databaseId: Int = 1
        , var connectionFileId: Int = 1
        , var password: String = "")