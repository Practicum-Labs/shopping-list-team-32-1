package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow

class ShoppingItemRepositoryImpl(
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingItemRepository {

    override fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItemEntity>> {
        return shoppingItemDao.getItemsForListFlow(listId)
    }

    override suspend fun getItemsForList(listId: Long): List<ShoppingItemEntity> {
        return shoppingItemDao.getItemsForList(listId)
    }

    override suspend fun insertItem(item: ShoppingItemEntity): Long {
        return shoppingItemDao.insertItem(item)
    }

    override suspend fun insertItems(items: List<ShoppingItemEntity>) {
        shoppingItemDao.insertItems(items)
    }

    override suspend fun updateItem(item: ShoppingItemEntity) {
        shoppingItemDao.updateItem(item)
    }

    override suspend fun deleteItem(item: ShoppingItemEntity) {
        shoppingItemDao.deleteItem(item)
    }

    override suspend fun clearBoughtItems(listId: Long) {
        shoppingItemDao.deleteBoughtItemsForList(listId)
    }

    override suspend fun clearAllItems(listId: Long) {
        shoppingItemDao.deleteItemsForList(listId)
    }

    override suspend fun updateItems(items: List<ShoppingItemEntity>) {
        shoppingItemDao.updateItems(items)
    }

    override fun getSuggestionsFlow(query: String): Flow<List<ProductSuggestionEntity>> {
        return shoppingItemDao.getSuggestionsFlow(query)
    }

    override suspend fun addSuggestion(name: String) {
        if (name.isNotBlank()) {
            shoppingItemDao.insertSuggestion(ProductSuggestionEntity(name = name.trim()))
        }
    }
}
