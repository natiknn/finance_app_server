package com.example.repository

import com.example.db.DatabaseFactory
import com.example.dto.CategoryDto
import com.example.dto.CreateCategoryRequest

class CategoryRepository {

    fun getCategories(userId: Int, type: String): List<CategoryDto> {
        val result = mutableListOf<CategoryDto>()

        DatabaseFactory.getConnection().use { connection ->
            val sql = """
                SELECT "CategoryId", "UserId", "Name", "Type", "ColorHex"
                FROM "Categories"
                WHERE "UserId" = ? AND "Type" = ?
                ORDER BY "Name"
            """.trimIndent()

            connection.prepareStatement(sql).use { statement ->
                statement.setInt(1, userId)
                statement.setString(2, type)

                val rs = statement.executeQuery()
                while (rs.next()) {
                    result.add(
                        CategoryDto(
                            categoryId = rs.getInt("CategoryId"),
                            userId = rs.getInt("UserId"),
                            name = rs.getString("Name"),
                            type = rs.getString("Type"),
                            colorHex = rs.getString("ColorHex")
                        )
                    )
                }
            }
        }

        return result
    }

    fun createCategory(request: CreateCategoryRequest): CategoryDto {
        DatabaseFactory.getConnection().use { connection ->
            val sql = """
                INSERT INTO "Categories" ("UserId", "Name", "Type", "ColorHex")
                VALUES (?, ?, ?, ?)
                RETURNING "CategoryId", "UserId", "Name", "Type", "ColorHex"
            """.trimIndent()

            connection.prepareStatement(sql).use { statement ->
                statement.setInt(1, request.userId)
                statement.setString(2, request.name)
                statement.setString(3, request.type)
                statement.setString(4, request.colorHex)

                val rs = statement.executeQuery()
                if (rs.next()) {
                    return CategoryDto(
                        categoryId = rs.getInt("CategoryId"),
                        userId = rs.getInt("UserId"),
                        name = rs.getString("Name"),
                        type = rs.getString("Type"),
                        colorHex = rs.getString("ColorHex")
                    )
                }
            }
        }

        error("Failed to create category")
    }
}
