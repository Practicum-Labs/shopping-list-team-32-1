package com.practicum.shoppinglist.domain.repository

import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingItemRepository {
    fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItemEntity>>
    suspend fun getItemsForList(listId: Long): List<ShoppingItemEntity>
    suspend fun insertItem(item: ShoppingItemEntity): Long
    suspend fun insertItems(items: List<ShoppingItemEntity>)
    suspend fun updateItem(item: ShoppingItemEntity)
    suspend fun deleteItem(item: ShoppingItemEntity)
    suspend fun clearBoughtItems(listId: Long)
    suspend fun clearAllItems(listId: Long)
    suspend fun updateItems(items: List<ShoppingItemEntity>)

    fun getSuggestionsFlow(query: String): Flow<List<ProductSuggestionEntity>>
    suspend fun addSuggestion(name: String)
}
