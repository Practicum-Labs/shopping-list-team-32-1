package com.practicum.shoppinglist.domain.model

data class ShoppingItem(
    val id: Long = 0,
    val listId: Long,
    val name: String,
    val quantity: Double,
    val unit: String,
    val isBought: Boolean = false,
    val sortOrder: Int = 0
)
