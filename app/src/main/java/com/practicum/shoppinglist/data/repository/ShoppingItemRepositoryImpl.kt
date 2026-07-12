package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.mapper.toDomain
import com.practicum.shoppinglist.data.mapper.toEntity
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.AuthRepository
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ShoppingItemRepositoryImpl(
    private val shoppingItemDao: ShoppingItemDao,
    private val shoppingListDao: ShoppingListDao,
    private val authRepository: AuthRepository,
) : ShoppingItemRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItem>> {
        return authRepository.currentUserId
            .flatMapLatest { ownerUserId ->
                if (ownerUserId == null) {
                    flowOf(emptyList())
                } else {
                    shoppingItemDao.getItemsForListFlow(listId, ownerUserId)
                }
            }
            .map { list ->
                list.map { it.toDomain() }
            }
    }

    override suspend fun getItemsForList(listId: Long): List<ShoppingItem> {
        val ownerUserId = authRepository.requireCurrentUserId()
        return shoppingItemDao.getItemsForList(listId, ownerUserId).map { it.toDomain() }
    }

    override suspend fun insertItem(item: ShoppingItem): Long {
        val ownerUserId = authRepository.requireCurrentUserId()
        checkListOwnership(item.listId, ownerUserId)
        return shoppingItemDao.insertItem(item.toEntity())
    }

    override suspend fun insertItems(items: List<ShoppingItem>) {
        if (items.isEmpty()) return
        val ownerUserId = authRepository.requireCurrentUserId()
        items.map { it.listId }.distinct().forEach { listId ->
            checkListOwnership(listId, ownerUserId)
        }
        shoppingItemDao.insertItems(items.map { it.toEntity() })
    }

    override suspend fun updateItem(item: ShoppingItem) {
        val ownerUserId = authRepository.requireCurrentUserId()
        checkListOwnership(item.listId, ownerUserId)
        shoppingItemDao.updateItem(item.toEntity())
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        val ownerUserId = authRepository.requireCurrentUserId()
        checkListOwnership(item.listId, ownerUserId)
        shoppingItemDao.deleteItem(item.toEntity())
    }

    override suspend fun clearBoughtItems(listId: Long) {
        val ownerUserId = authRepository.requireCurrentUserId()
        shoppingItemDao.deleteBoughtItemsForList(listId, ownerUserId)
    }

    override suspend fun deleteAllItems(listId: Long) {
        val ownerUserId = authRepository.requireCurrentUserId()
        shoppingItemDao.deleteItemsForList(listId, ownerUserId)
    }

    override suspend fun updateItems(items: List<ShoppingItem>) {
        if (items.isEmpty()) return
        val ownerUserId = authRepository.requireCurrentUserId()
        items.map { it.listId }.distinct().forEach { listId ->
            checkListOwnership(listId, ownerUserId)
        }
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

    private suspend fun checkListOwnership(listId: Long, ownerUserId: Long) {
        val listExists = shoppingListDao.getShoppingListById(listId, ownerUserId) != null
        if (!listExists) {
            throw SecurityException("List $listId not found or access denied for user $ownerUserId")
        }
    }
}
