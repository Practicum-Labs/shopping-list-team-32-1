package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

internal class FakeShoppingItemDao : ShoppingItemDao {
    val suggestionsFlow = MutableSharedFlow<List<ProductSuggestionEntity>>(replay = 1)
    val insertedSuggestions = mutableListOf<ProductSuggestionEntity>()
    val insertedItems = mutableListOf<ShoppingItemEntity>()
    val insertedItemLists = mutableListOf<List<ShoppingItemEntity>>()
    val updatedItems = mutableListOf<ShoppingItemEntity>()
    val updatedItemLists = mutableListOf<List<ShoppingItemEntity>>()
    val deletedItems = mutableListOf<ShoppingItemEntity>()
    val deletedListRequests = mutableListOf<ListRequest>()
    val deletedBoughtListRequests = mutableListOf<ListRequest>()
    val requestedListIds = mutableListOf<ListRequest>()
    var itemsForList = emptyList<ShoppingItemEntity>()
    var insertedItemId = INSERTED_ITEM_ID

    override fun getItemsForListFlow(listId: Long, ownerUserId: Long): Flow<List<ShoppingItemEntity>> {
        requestedListIds.add(ListRequest(listId = listId, ownerUserId = ownerUserId))
        return flowOf(itemsForList)
    }

    override suspend fun getItemsForList(listId: Long, ownerUserId: Long): List<ShoppingItemEntity> {
        requestedListIds.add(ListRequest(listId = listId, ownerUserId = ownerUserId))
        return itemsForList
    }

    override suspend fun insertItem(item: ShoppingItemEntity): Long {
        insertedItems.add(item)
        return insertedItemId
    }

    override suspend fun insertItems(items: List<ShoppingItemEntity>) {
        insertedItemLists.add(items)
    }

    override suspend fun updateItem(item: ShoppingItemEntity) {
        updatedItems.add(item)
    }

    override suspend fun updateItems(items: List<ShoppingItemEntity>) {
        updatedItemLists.add(items)
    }

    override suspend fun deleteItem(item: ShoppingItemEntity) {
        deletedItems.add(item)
    }

    override suspend fun deleteItemsForList(listId: Long, ownerUserId: Long) {
        deletedListRequests.add(ListRequest(listId = listId, ownerUserId = ownerUserId))
    }

    override suspend fun deleteBoughtItemsForList(listId: Long, ownerUserId: Long) {
        deletedBoughtListRequests.add(ListRequest(listId = listId, ownerUserId = ownerUserId))
    }

    override fun getSuggestionsFlow(query: String): Flow<List<ProductSuggestionEntity>> = suggestionsFlow

    override suspend fun insertSuggestion(suggestion: ProductSuggestionEntity) {
        insertedSuggestions.add(suggestion)
    }

    data class ListRequest(
        val listId: Long,
        val ownerUserId: Long,
    )

    private companion object {
        const val INSERTED_ITEM_ID = 100L
    }
}

internal class FakeShoppingListDao : ShoppingListDao {
    val shoppingListsFlow = MutableSharedFlow<List<ShoppingListEntity>>(replay = 1)
    val insertedLists = mutableListOf<ShoppingListEntity>()
    val iconUpdates = mutableListOf<IconUpdate>()
    val deletedAllOwnerIds = mutableListOf<Long>()
    val deletedLists = mutableListOf<ListRequest>()
    val nameUpdates = mutableListOf<NameUpdate>()
    val sortTypeUpdates = mutableListOf<SortTypeUpdate>()
    var shoppingListById: ShoppingListEntity? = null
    var insertedListId = INSERTED_LIST_ID

    override fun observeShoppingLists(ownerUserId: Long): Flow<List<ShoppingListEntity>> = shoppingListsFlow

    override suspend fun getShoppingListById(shoppingListId: Long, ownerUserId: Long): ShoppingListEntity? {
        return shoppingListById
    }

    override suspend fun getShoppingListById(shoppingListId: Long): ShoppingListEntity? {
        return shoppingListById
    }

    override suspend fun insertShoppingList(shoppingList: ShoppingListEntity): Long {
        insertedLists.add(shoppingList)
        return insertedListId
    }

    override suspend fun updateShoppingListIcon(
        shoppingListId: Long,
        iconName: String,
        ownerUserId: Long,
    ) {
        iconUpdates.add(
            IconUpdate(
                shoppingListId = shoppingListId,
                iconName = iconName,
                ownerUserId = ownerUserId,
            ),
        )
    }

    override suspend fun deleteAllShoppingLists(ownerUserId: Long) {
        deletedAllOwnerIds.add(ownerUserId)
    }

    override suspend fun deleteShoppingList(shoppingListId: Long, ownerUserId: Long) {
        deletedLists.add(ListRequest(shoppingListId = shoppingListId, ownerUserId = ownerUserId))
    }

    override suspend fun updateShoppingListName(shoppingListId: Long, name: String, ownerUserId: Long) {
        nameUpdates.add(
            NameUpdate(
                shoppingListId = shoppingListId,
                name = name,
                ownerUserId = ownerUserId,
            ),
        )
    }

    override suspend fun updateShoppingListSortType(shoppingListId: Long, sortType: String, ownerUserId: Long) {
        sortTypeUpdates.add(
            SortTypeUpdate(
                shoppingListId = shoppingListId,
                sortType = sortType,
                ownerUserId = ownerUserId,
            ),
        )
    }

    data class ListRequest(
        val shoppingListId: Long,
        val ownerUserId: Long,
    )

    data class IconUpdate(
        val shoppingListId: Long,
        val iconName: String,
        val ownerUserId: Long,
    )

    data class NameUpdate(
        val shoppingListId: Long,
        val name: String,
        val ownerUserId: Long,
    )

    data class SortTypeUpdate(
        val shoppingListId: Long,
        val sortType: String,
        val ownerUserId: Long,
    )

    private companion object {
        const val INSERTED_LIST_ID = 100L
    }
}
