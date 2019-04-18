package model

data class DatabasePasswordFile(
        var databaseId: Int
        , var connectionFileId: Int
        , var password: String)