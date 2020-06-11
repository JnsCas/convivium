package com.jnscas

import io.ktor.application.*
import io.ktor.routing.*
import com.fasterxml.jackson.databind.*
import com.jnscas.controllers.foodController
import com.jnscas.persistence.FoodsTable
import com.jnscas.services.FoodDSL
import com.typesafe.config.ConfigFactory
import io.ktor.jackson.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(DefaultHeaders)

    val database: Database = initDatabase()

    val foodDSL = FoodDSL(database)
    val url = "convivium"

    routing {
        foodController(url, foodDSL)
    }
}

private fun initDatabase(): Database {
    val config = ConfigFactory.load()
    val urlDatabase = config.getString("ktor.database.url")
    val driverDatabase = config.getString("ktor.database.driver")

    val database: Database = Database.connect(urlDatabase, driver = driverDatabase)
    transaction(database) {
        SchemaUtils.create(FoodsTable)
    }
    return database
}

