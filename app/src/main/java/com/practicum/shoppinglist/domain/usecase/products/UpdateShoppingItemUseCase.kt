package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ProductSuggestionRepository
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

class UpdateShoppingItemUseCase(
    private val shoppingItemRepository: ShoppingItemRepository,
    private val productSuggestionRepository: ProductSuggestionRepository
) {
    suspend operator fun invoke(item: ShoppingItem, name: String, quantity: Double, unit: String) {
        val trimmedName = name.trim()
        productSuggestionRepository.addSuggestion(trimmedName)
        shoppingItemRepository.updateItem(item.copy(name = trimmedName, quantity = quantity, unit = unit))
    }
}
