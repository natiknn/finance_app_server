package com.example.db

import java.sql.Connection
import java.sql.DriverManager


object DatabaseFactory {
    private const val URL =
        "jdbc:sqlserver://localhost:1433;databaseName=Finance_1;encrypt=false;trustServerCertificate=true"
    private const val USER = "finance_user"
    private const val PASSWORD = "Finance123!"

    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}
