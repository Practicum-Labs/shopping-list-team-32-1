package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class DeleteAllShoppingItemsUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    suspend operator fun invoke(listId: Long) {
        shoppingItemRepository.deleteAllItems(listId)
    }
}
