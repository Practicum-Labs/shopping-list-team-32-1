package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class AddProductSuggestionUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    suspend operator fun invoke(name: String) {
        shoppingItemRepository.addSuggestion(name)
    }
}
