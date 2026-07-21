package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class ToggleShoppingItemBoughtUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    suspend operator fun invoke(item: ShoppingItem) {
        shoppingItemRepository.updateItem(item.copy(isBought = !item.isBought))
    }
}
