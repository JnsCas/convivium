package com.jnscas.controllers

import com.jnscas.api.FoodRequest
import com.jnscas.controllers.api.RandomRequest
import com.jnscas.model.Food
import com.jnscas.services.FoodDSL
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

fun Route.foodController(url: String, foodDSL: FoodDSL) {

    val path = "/food"

    get("$url/$path") {
        call.respond(foodDSL.selectAll())
    }

    get("$url/$path/random/{category}") {
        val request: RandomRequest = RandomRequest.create(call)

        if (request.category == null) {
            call.respond(HttpStatusCode.BadRequest) //category not exists in enum
        }

        val candidates: List<Food> = if (request.available == null) {
            foodDSL.selectAllByCategory(request.category!!)
        } else {
            foodDSL.selectAllByCategoryAndAvailability(request.category!!, request.available)
        }

        if (candidates.isEmpty()) {
            call.respond(HttpStatusCode.NotFound)
        }

        var foodRandom: Food = candidates.random()
        while (candidates.size > 1 && foodRandom.name == request.last) {
            foodRandom = candidates.random()
        }
        call.respond(foodRandom)
    }

    post("$url/$path") {
        val request: FoodRequest = call.receive()
        if (request.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
        }

        if (foodDSL.createFood(request.name, request.available, request.category)) {
            call.respond(HttpStatusCode.Created, "Created")
        } else {
            call.respond(HttpStatusCode.OK, "Food already exists.")
        }
    }

}