package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val categoryId: Int,
    val userId: Int,
    val name: String,
    val type: String,
    val colorHex: String? = null
)
