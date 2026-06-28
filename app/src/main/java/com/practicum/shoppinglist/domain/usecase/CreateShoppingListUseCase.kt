package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class CreateShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(
        name: String,
        iconName: String,
    ): Long {
        return shoppingListRepository.createShoppingList(
            name = name,
            iconName = iconName,
        )
    }
}
