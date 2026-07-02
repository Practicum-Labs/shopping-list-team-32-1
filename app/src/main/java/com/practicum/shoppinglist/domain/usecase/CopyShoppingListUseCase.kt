package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class CopyShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(copiedName: String, iconName: String): Long {
        return shoppingListRepository.createShoppingList(
            name = copiedName,
            iconName = iconName,
        )
    }
}
