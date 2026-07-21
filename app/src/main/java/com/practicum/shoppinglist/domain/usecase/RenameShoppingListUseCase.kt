package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class RenameShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(shoppingListId: Long, name: String) {
        shoppingListRepository.updateShoppingListName(shoppingListId, name)
    }
}
