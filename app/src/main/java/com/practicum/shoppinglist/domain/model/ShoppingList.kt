package com.practicum.shoppinglist.domain.model

data class ShoppingList(
    val id: Long = 0L,
    val name: String,
    val iconName: String,
    val items: List<ShoppingItem> = emptyList(),
    val sortType: String = "CUSTOM",
)
