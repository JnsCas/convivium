package com.jnscas.model

import com.jnscas.persistence.FoodCategory

data class Food(val id: Int,
                val name: String,
                val available: Boolean,
                val category: FoodCategory) {
    override fun equals(other: Any?): Boolean {
        if (other is Food) {
            return other.name == this.name
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        fun create(id: Int, name: String,
                   available: Boolean,
                   category: FoodCategory): Food {
            return Food(
                id = id,
                name = name,
                available = available,
                category = category
            )
        }
    }
}