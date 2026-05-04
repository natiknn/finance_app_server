package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto (
    val transactionId: Int,
    val userId: Int,
    val categoryId: Int,
    val categoryName: String,
    val colorHex: String? = null,
    val type: String,
    val amount: Double,
    val comment: String? = null,
    val transactionDate: String
)