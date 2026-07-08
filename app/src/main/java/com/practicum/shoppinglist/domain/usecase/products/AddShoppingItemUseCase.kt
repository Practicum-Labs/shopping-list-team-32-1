package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class AddShoppingItemUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        listId: Long,
        name: String,
        quantity: Double,
        unit: String,
        currentItems: List<ShoppingItem>
    ) {
        val trimmedName = name.trim()
        shoppingItemRepository.addSuggestion(trimmedName)
        val minOrder = currentItems.minOfOrNull { it.sortOrder } ?: 0
        val newItem = ShoppingItem(
            listId = listId,
            name = trimmedName,
            quantity = quantity,
            unit = unit,
            sortOrder = minOrder - 1
        )
        shoppingItemRepository.insertItem(newItem)
    }
}
