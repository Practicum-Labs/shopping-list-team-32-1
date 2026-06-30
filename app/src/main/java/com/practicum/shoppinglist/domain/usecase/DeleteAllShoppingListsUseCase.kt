package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class DeleteAllShoppingListsUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke() {
        shoppingListRepository.deleteAllShoppingLists()
    }
}
