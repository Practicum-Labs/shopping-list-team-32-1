package com.practicum.shoppinglist.domain.repository

import com.practicum.shoppinglist.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeShoppingLists(): Flow<List<ShoppingList>>

    suspend fun getShoppingListById(shoppingListId: Long): ShoppingList?

    suspend fun createShoppingList(
        name: String,
        iconName: String,
    ): Long

    suspend fun updateShoppingListIcon(
        shoppingListId: Long,
        iconName: String,
    )

    suspend fun deleteAllShoppingLists()

    suspend fun deleteShoppingList(shoppingListId: Long)

    suspend fun updateShoppingListName(
        shoppingListId: Long,
        name: String,
    )
}
