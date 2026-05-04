package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryRequest(
    val userId: Int,
    val name: String,
    val type: String,
    val colorHex: String? = null
)
