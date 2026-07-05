package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class CopyShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
) {
    suspend operator fun invoke(originalListId: Long, copiedName: String, iconName: String): Long {
        // 1. Create the new list metadata
        val newListId = shoppingListRepository.createShoppingList(
            name = copiedName,
            iconName = iconName,
        )
        // 2. Fetch the items from the original list
        val originalItems = shoppingItemRepository.getItemsForList(originalListId)
        // 3. Duplicate items mapping them to the new list id
        val duplicatedItems = originalItems.map { item ->
            item.copy(id = 0, listId = newListId)
        }
        // 4. Insert duplicated items
        shoppingItemRepository.insertItems(duplicatedItems)
        return newListId
    }
}
