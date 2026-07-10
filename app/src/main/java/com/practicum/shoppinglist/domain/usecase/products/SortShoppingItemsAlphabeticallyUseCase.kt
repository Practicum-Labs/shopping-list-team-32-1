package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class SortShoppingItemsAlphabeticallyUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    suspend operator fun invoke(items: List<ShoppingItem>) {
        val sorted = items.sortedBy { it.name.lowercase() }
        val updated = sorted.mapIndexed { index, item -> item.copy(sortOrder = index) }
        shoppingItemRepository.updateItems(updated)
    }
}
