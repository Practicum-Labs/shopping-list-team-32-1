package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.data.mapper.toDomain
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.repository.AuthRepository
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
    private val authRepository: AuthRepository,
) : ShoppingListRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeShoppingLists(): Flow<List<ShoppingList>> {
        return authRepository.currentUserId
            .flatMapLatest { ownerUserId ->
                if (ownerUserId == null) {
                    flowOf(emptyList())
                } else {
                    shoppingListDao.observeShoppingLists(ownerUserId = ownerUserId)
                }
            }
            .map { shoppingListEntities ->
                shoppingListEntities.map { shoppingList -> shoppingList.toDomain() }
            }
    }

    override suspend fun getShoppingListById(shoppingListId: Long): ShoppingList? {
        val ownerUserId = authRepository.requireCurrentUserId()
        return shoppingListDao.getShoppingListById(shoppingListId, ownerUserId)?.toDomain()
    }

    override suspend fun createShoppingList(
        name: String,
        iconName: String,
    ): Long {
        val ownerUserId = authRepository.requireCurrentUserId()
        return shoppingListDao.insertShoppingList(
            ShoppingListEntity(
                name = name,
                iconName = iconName,
                ownerUserId = ownerUserId,
            ),
        )
    }

    override suspend fun updateShoppingListIcon(
        shoppingListId: Long,
        iconName: String,
    ) {
        val ownerUserId = authRepository.requireCurrentUserId()
        shoppingListDao.updateShoppingListIcon(
            shoppingListId = shoppingListId,
            iconName = iconName,
            ownerUserId = ownerUserId,
        )
    }

    override suspend fun deleteAllShoppingLists() {
        shoppingListDao.deleteAllShoppingLists(
            ownerUserId = authRepository.requireCurrentUserId(),
        )
    }

    override suspend fun deleteShoppingList(shoppingListId: Long) {
        shoppingListDao.deleteShoppingList(
            shoppingListId = shoppingListId,
            ownerUserId = authRepository.requireCurrentUserId(),
        )
    }

    override suspend fun updateShoppingListName(shoppingListId: Long, name: String) {
        shoppingListDao.updateShoppingListName(
            shoppingListId = shoppingListId,
            name = name,
            ownerUserId = authRepository.requireCurrentUserId(),
        )
    }
}
