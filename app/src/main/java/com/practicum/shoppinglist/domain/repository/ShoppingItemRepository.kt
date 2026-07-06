package com.practicum.shoppinglist.domain.repository

import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingItemRepository {
    fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItem>>
    suspend fun getItemsForList(listId: Long): List<ShoppingItem>
    suspend fun insertItem(item: ShoppingItem): Long
    suspend fun insertItems(items: List<ShoppingItem>)
    suspend fun updateItem(item: ShoppingItem)
    suspend fun deleteItem(item: ShoppingItem)
    suspend fun clearBoughtItems(listId: Long)
    suspend fun updateItems(items: List<ShoppingItem>)

    fun getSuggestionsFlow(query: String): Flow<List<ProductSuggestionEntity>>
    suspend fun addSuggestion(name: String)
}
