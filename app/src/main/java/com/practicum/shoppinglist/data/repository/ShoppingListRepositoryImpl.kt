package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.data.mapper.toDomain
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListRepository {
    override fun observeShoppingLists(): Flow<List<ShoppingList>> {
        return shoppingListDao.observeShoppingLists()
            .map { shoppingLists ->
                shoppingLists.map { shoppingList -> shoppingList.toDomain() }
            }
    }

    override suspend fun createShoppingList(
        name: String,
        iconName: String,
    ): Long {
        return shoppingListDao.insertShoppingList(
            ShoppingListEntity(
                name = name,
                iconName = iconName,
            ),
        )
    }

    override suspend fun updateShoppingListIcon(
        shoppingListId: Long,
        iconName: String,
    ) {
        shoppingListDao.updateShoppingListIcon(
            shoppingListId = shoppingListId,
            iconName = iconName,
        )
    }
}
