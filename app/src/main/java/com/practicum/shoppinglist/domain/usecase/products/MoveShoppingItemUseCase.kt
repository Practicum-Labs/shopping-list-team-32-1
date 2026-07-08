package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class MoveShoppingItemUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    suspend operator fun invoke(fromIndex: Int, toIndex: Int, items: List<ShoppingItem>): List<ShoppingItem> {
        val currentItems = items.toMutableList()
        if (fromIndex in currentItems.indices && toIndex in currentItems.indices) {
            val item = currentItems.removeAt(fromIndex)
            currentItems.add(toIndex, item)
            val updated = currentItems.mapIndexed { index, it -> it.copy(sortOrder = index) }
            shoppingItemRepository.updateItems(updated)
            return updated
        }
        return items
    }
}
