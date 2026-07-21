package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class GetShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(listId: Long): ShoppingList? {
        return shoppingListRepository.getShoppingListById(listId)
    }
}
