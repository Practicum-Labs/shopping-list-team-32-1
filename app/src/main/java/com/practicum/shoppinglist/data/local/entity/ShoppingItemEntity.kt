package com.practicum.shoppinglist.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "listId")
    val listId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "quantity")
    val quantity: Double,
    @ColumnInfo(name = "unit")
    val unit: String, // Can be "шт.", "кг.", "л.", "мл.", "г." or empty
    @ColumnInfo(name = "isBought")
    val isBought: Boolean = false,
    @ColumnInfo(name = "sortOrder")
    val sortOrder: Int = 0
)

@Entity(tableName = "product_suggestions")
data class ProductSuggestionEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String
)
