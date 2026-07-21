package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class DeleteShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(shoppingListId: Long) {
        shoppingListRepository.deleteShoppingList(shoppingListId)
    }
}
