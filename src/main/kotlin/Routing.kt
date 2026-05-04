package com.example

import com.example.financeserver.dto.CreateTransactionRequest
import com.example.repository.CategoryRepository
import com.example.repository.TransactionRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        val transactionsRepository = TransactionRepository()
        val categoryRepository = CategoryRepository()

        get("/") {
            call.respondText("Hello World!")
        }

        route("/api/transactions") {
            get {
                val userId = call.request.queryParameters["userId"]?.toIntOrNull()

                if (userId == null) {
                    call.respond(HttpStatusCode.BadRequest, "userId is required")
                    return@get
                }

                val transactions = transactionsRepository.getTransactions(userId)
                call.respond(transactions)
            }

            post {
                val request = call.receive<CreateTransactionRequest>()
                val created = transactionsRepository.createTransaction(request)
                call.respond(created)
            }
        }
        route("api/categories") {
            get {
                val userId = call.request.queryParameters["userId"]?.toIntOrNull()
                val type = call.request.queryParameters["type"]

                if (userId == null || type == null) {
                    call.respond(HttpStatusCode.BadRequest, "userId and type are required")
                    return@get
                }
                val categories = categoryRepository.getCategories(userId, type)
                call.respond(categories)
            }
        }
    }
}
