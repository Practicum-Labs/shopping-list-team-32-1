package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.repository.ShoppingListRepository

class UpdateShoppingListSortTypeUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(shoppingListId: Long, sortType: String) {
        shoppingListRepository.updateShoppingListSortType(shoppingListId, sortType)
    }
}
