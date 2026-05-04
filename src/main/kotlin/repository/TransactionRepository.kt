package com.example.repository

import com.example.db.DatabaseFactory
import com.example.dto.TransactionDto
import com.example.financeserver.dto.CreateTransactionRequest

class TransactionRepository {

    fun getTransactions(userId: Int): List<TransactionDto> {
        val result = mutableListOf<TransactionDto>()

        DatabaseFactory.getConnection().use { connection ->
            val sql = """
                SELECT
                    t.TransactionId,
                    t.UserId,
                    t.CategoryId,
                    c.Name AS CategoryName,
                    c.ColorHex,
                    t.Type,
                    t.Amount,
                    t.Comment,
                    t.TransactionDate
                FROM Transactions t
                JOIN Categories c ON t.CategoryId = c.CategoryId
                WHERE t.UserId = ?
                ORDER BY t.TransactionDate DESC, t.TransactionId DESC
            """.trimIndent()

            connection.prepareStatement(sql).use { statement ->
                statement.setInt(1, userId)

                val rs = statement.executeQuery()
                while (rs.next()) {
                    result.add(
                        TransactionDto(
                            transactionId = rs.getInt("TransactionId"),
                            userId = rs.getInt("UserId"),
                            categoryId = rs.getInt("CategoryId"),
                            categoryName = rs.getString("CategoryName"),
                            colorHex = rs.getString("ColorHex"),
                            type = rs.getString("Type"),
                            amount = rs.getBigDecimal("Amount").toDouble(),
                            comment = rs.getString("Comment"),
                            transactionDate = rs.getDate("TransactionDate").toString()
                        )
                    )
                }
            }
        }

        return result
    }

    fun createTransaction(request: CreateTransactionRequest): TransactionDto {
        DatabaseFactory.getConnection().use { connection ->
            val insertSql = """
                INSERT INTO Transactions (UserId, CategoryId, Type, Amount, Comment, TransactionDate)
                OUTPUT INSERTED.TransactionId
                VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent()

            val transactionId = connection.prepareStatement(insertSql).use { statement ->
                statement.setInt(1, request.userId)
                statement.setInt(2, request.categoryId)
                statement.setString(3, request.type)
                statement.setBigDecimal(4, request.amount.toBigDecimal())
                statement.setString(5, request.comment)
                statement.setDate(6, java.sql.Date.valueOf(request.transactionDate))

                val rs = statement.executeQuery()
                if (rs.next()) rs.getInt(1) else error("Insert failed")
            }

            val selectSql = """
                SELECT
                    t.TransactionId,
                    t.UserId,
                    t.CategoryId,
                    c.Name AS CategoryName,
                    c.ColorHex,
                    t.Type,
                    t.Amount,
                    t.Comment,
                    t.TransactionDate
                FROM Transactions t
                JOIN Categories c ON t.CategoryId = c.CategoryId
                WHERE t.TransactionId = ?
            """.trimIndent()

            connection.prepareStatement(selectSql).use { statement ->
                statement.setInt(1, transactionId)

                val rs = statement.executeQuery()
                if (rs.next()) {
                    return TransactionDto(
                        transactionId = rs.getInt("TransactionId"),
                        userId = rs.getInt("UserId"),
                        categoryId = rs.getInt("CategoryId"),
                        categoryName = rs.getString("CategoryName"),
                        colorHex = rs.getString("ColorHex"),
                        type = rs.getString("Type"),
                        amount = rs.getBigDecimal("Amount").toDouble(),
                        comment = rs.getString("Comment"),
                        transactionDate = rs.getDate("TransactionDate").toString()
                    )
                }
            }
        }

        error("Failed to create transaction")
    }

}