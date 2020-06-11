package com.jnscas.services

import com.jnscas.model.Food
import com.jnscas.persistence.FoodCategory
import com.jnscas.persistence.FoodsTable
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FoodDSL(private val database: Database)  {
    fun createFood(name: String, available: Boolean?, category: FoodCategory): Boolean {
        return transaction(database) {
            try {
                FoodsTable.insertAndGetId {
                    it[FoodsTable.name] = name
                    it[FoodsTable.available] = available ?: false
                    it[FoodsTable.category] = category.toString()
                }
                exposedLogger.info("Food '$name' inserted.")
                true
            } catch (e: ExposedSQLException) {
                if (e.cause is JdbcSQLIntegrityConstraintViolationException) {
                    exposedLogger.info("The name food '$name' already exists in FOODS table.")
                    false
                } else {
                    throw e
                }
            }
        }
    }

    fun selectAll(): List<Food> {
        return transaction(database) {
            FoodsTable.selectAll()
                .map {
                    buildFood(it)
                }
        }
    }

    fun selectAllByCategory(category: FoodCategory): List<Food> {
        return transaction(database) {
            FoodsTable.select {
                FoodsTable.category eq category.toString()
            }.map {
                buildFood(it)
            }
        }
    }

    fun selectAllByCategoryAndAvailability(category: FoodCategory,
                                           available: Boolean): List<Food> {
        return transaction(database) {
            FoodsTable.select {
                FoodsTable.category eq category.toString() and (FoodsTable.available eq available)
            }.map {
                buildFood(it)
            }
        }
    }

    private fun buildFood(it: ResultRow): Food {
        return Food.create(
            id = it[FoodsTable.id].value,
            name = it[FoodsTable.name],
            available = it[FoodsTable.available],
            category = FoodCategory.valueOf(it[FoodsTable.category])
        )
    }
}