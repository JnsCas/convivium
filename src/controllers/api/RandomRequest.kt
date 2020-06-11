package com.jnscas.controllers.api

import com.jnscas.persistence.FoodCategory
import io.ktor.application.ApplicationCall

data class RandomRequest(val category: FoodCategory?,
                         val available: Boolean?,
                         val last: String? ) {
    companion object {
        fun create(call: ApplicationCall): RandomRequest {
            val category: FoodCategory? = try {
                FoodCategory.valueOf(call.parameters["category"]!!.toUpperCase())
            } catch (e: IllegalArgumentException) {
                null
            }
            return RandomRequest(
                category = category,
                available = call.request.queryParameters["available"]?.toBoolean(),
                last = call.request.queryParameters["last"]
            )
        }
    }
}