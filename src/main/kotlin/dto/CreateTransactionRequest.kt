package com.example.financeserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionRequest(
    val userId: Int,
    val categoryId: Int,
    val type: String,
    val amount: Double,
    val comment: String? = null,
    val transactionDate: String
)
