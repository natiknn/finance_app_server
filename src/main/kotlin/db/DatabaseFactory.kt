package com.example.db

import java.sql.Connection
import java.sql.DriverManager
import io.github.cdimascio.dotenv.dotenv


object DatabaseFactory {
    val dotenv = dotenv()

    val dbUrl = dotenv["DB_URL"]
    val dbUser = dotenv["DB_USER"]
    val dbPassword = dotenv["DB_PASSWORD"]
    private val URL = dbUrl
    private val USER = dbUser
    private val PASSWORD = dbPassword

    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}
