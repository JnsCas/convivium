package com.jnscas.persistence

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

enum class FoodCategory {
    PRIMARY,
    SECONDARY

}

object FoodsTable : IntIdTable() {
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val available: Column<Boolean> = bool("available")
    val category: Column<String> = varchar("category", 20)
}