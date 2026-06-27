package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class UpdateShoppingListIconUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(
        shoppingListId: Long,
        iconName: String,
    ) {
        shoppingListRepository.updateShoppingListIcon(
            shoppingListId = shoppingListId,
            iconName = iconName,
        )
    }
}
