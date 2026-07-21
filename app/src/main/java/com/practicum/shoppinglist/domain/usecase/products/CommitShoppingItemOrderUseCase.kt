package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class CommitShoppingItemOrderUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    suspend operator fun invoke(items: List<ShoppingItem>) {
        shoppingItemRepository.updateItems(items)
    }
}
