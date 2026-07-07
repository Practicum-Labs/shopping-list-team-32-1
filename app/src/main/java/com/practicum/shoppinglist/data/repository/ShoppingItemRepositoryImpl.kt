package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.mapper.toDomain
import com.practicum.shoppinglist.data.mapper.toEntity
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingItemRepositoryImpl(
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingItemRepository {

    override fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItem>> {
        return shoppingItemDao.getItemsForListFlow(listId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getItemsForList(listId: Long): List<ShoppingItem> {
        return shoppingItemDao.getItemsForList(listId).map { it.toDomain() }
    }

    override suspend fun insertItem(item: ShoppingItem): Long {
        return shoppingItemDao.insertItem(item.toEntity())
    }

    override suspend fun insertItems(items: List<ShoppingItem>) {
        shoppingItemDao.insertItems(items.map { it.toEntity() })
    }

    override suspend fun updateItem(item: ShoppingItem) {
        shoppingItemDao.updateItem(item.toEntity())
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        shoppingItemDao.deleteItem(item.toEntity())
    }

    override suspend fun clearBoughtItems(listId: Long) {
        shoppingItemDao.deleteBoughtItemsForList(listId)
    }

    override suspend fun updateItems(items: List<ShoppingItem>) {
        shoppingItemDao.updateItems(items.map { it.toEntity() })
    }

    override fun getSuggestionsFlow(query: String): Flow<List<String>> {
        return shoppingItemDao.getSuggestionsFlow(query).map { list ->
            list.map { it.name }
        }
    }

    override suspend fun addSuggestion(name: String) {
        if (name.isNotBlank()) {
            shoppingItemDao.insertSuggestion(ProductSuggestionEntity(name = name.trim()))
        }
    }
}
