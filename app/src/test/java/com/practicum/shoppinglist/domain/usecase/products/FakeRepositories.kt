package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ProductSuggestionRepository
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

internal class FakeShoppingItemRepository : ShoppingItemRepository {
    var itemsForList = emptyList<ShoppingItem>()
    val requestedListIds = mutableListOf<Long>()
    val insertedItems = mutableListOf<ShoppingItem>()
    val insertedItemLists = mutableListOf<List<ShoppingItem>>()
    val updatedItems = mutableListOf<ShoppingItem>()
    val updatedItemLists = mutableListOf<List<ShoppingItem>>()
    val deletedItems = mutableListOf<ShoppingItem>()
    val clearedBoughtListIds = mutableListOf<Long>()
    val deletedAllListIds = mutableListOf<Long>()

    override fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItem>> {
        requestedListIds.add(listId)
        return flowOf(itemsForList)
    }

    override suspend fun getItemsForList(listId: Long): List<ShoppingItem> {
        requestedListIds.add(listId)
        return itemsForList
    }

    override suspend fun insertItem(item: ShoppingItem): Long {
        insertedItems.add(item)
        return item.id
    }

    override suspend fun insertItems(items: List<ShoppingItem>) {
        insertedItemLists.add(items)
    }

    override suspend fun updateItem(item: ShoppingItem) {
        updatedItems.add(item)
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        deletedItems.add(item)
    }

    override suspend fun clearBoughtItems(listId: Long) {
        clearedBoughtListIds.add(listId)
    }

    override suspend fun deleteAllItems(listId: Long) {
        deletedAllListIds.add(listId)
    }

    override suspend fun updateItems(items: List<ShoppingItem>) {
        updatedItemLists.add(items)
    }
}

internal class FakeProductSuggestionRepository : ProductSuggestionRepository {
    val addedSuggestions = mutableListOf<String>()
    val suggestionsFlow = MutableSharedFlow<List<String>>(replay = 1)
    val requestedQueries = mutableListOf<String>()

    override fun getSuggestionsFlow(query: String): Flow<List<String>> {
        requestedQueries.add(query)
        return suggestionsFlow
    }

    override suspend fun addSuggestion(name: String) {
        addedSuggestions.add(name)
    }
}
