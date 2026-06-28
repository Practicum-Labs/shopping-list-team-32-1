package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class ObserveShoppingListsUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    operator fun invoke() = shoppingListRepository.observeShoppingLists()
}
