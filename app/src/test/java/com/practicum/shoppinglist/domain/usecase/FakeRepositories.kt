package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import com.practicum.shoppinglist.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeShoppingListRepository : ShoppingListRepository {
    val shoppingListsFlow = MutableSharedFlow<List<ShoppingList>>(replay = 1)
    var shoppingListById: ShoppingList? = null
    var createdListId = CREATED_LIST_ID

    val createdLists = mutableListOf<CreatedShoppingList>()
    val updatedIcons = mutableListOf<UpdatedIcon>()
    val deletedListIds = mutableListOf<Long>()
    val renamedLists = mutableListOf<RenamedShoppingList>()
    val updatedSortTypes = mutableListOf<UpdatedSortType>()
    var deleteAllCallCount = 0

    override fun observeShoppingLists(): Flow<List<ShoppingList>> = shoppingListsFlow

    override suspend fun getShoppingListById(shoppingListId: Long): ShoppingList? = shoppingListById

    override suspend fun createShoppingList(name: String, iconName: String): Long {
        createdLists.add(CreatedShoppingList(name = name, iconName = iconName))
        return createdListId
    }

    override suspend fun updateShoppingListIcon(shoppingListId: Long, iconName: String) {
        updatedIcons.add(UpdatedIcon(shoppingListId = shoppingListId, iconName = iconName))
    }

    override suspend fun deleteAllShoppingLists() {
        deleteAllCallCount++
    }

    override suspend fun deleteShoppingList(shoppingListId: Long) {
        deletedListIds.add(shoppingListId)
    }

    override suspend fun updateShoppingListName(shoppingListId: Long, name: String) {
        renamedLists.add(RenamedShoppingList(shoppingListId = shoppingListId, name = name))
    }

    override suspend fun updateShoppingListSortType(shoppingListId: Long, sortType: String) {
        updatedSortTypes.add(UpdatedSortType(shoppingListId = shoppingListId, sortType = sortType))
    }

    data class CreatedShoppingList(
        val name: String,
        val iconName: String,
    )

    data class UpdatedIcon(
        val shoppingListId: Long,
        val iconName: String,
    )

    data class RenamedShoppingList(
        val shoppingListId: Long,
        val name: String,
    )

    data class UpdatedSortType(
        val shoppingListId: Long,
        val sortType: String,
    )

    private companion object {
        const val CREATED_LIST_ID = 10L
    }
}

internal class FakeThemeRepository : ThemeRepository {
    override val isDarkTheme = MutableStateFlow<Boolean?>(null)
    val savedThemeValues = mutableListOf<Boolean>()

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        savedThemeValues.add(isDarkTheme)
        this.isDarkTheme.value = isDarkTheme
    }
}
