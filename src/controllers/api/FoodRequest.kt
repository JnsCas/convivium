package com.jnscas.api

import com.jnscas.persistence.FoodCategory

data class FoodRequest(val name: String,
                       val available: Boolean?,
                       val category: FoodCategory)