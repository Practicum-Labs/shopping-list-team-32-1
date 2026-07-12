package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.repository.ProductSuggestionRepository

class AddProductSuggestionUseCase(
    private val productSuggestionRepository: ProductSuggestionRepository
) {
    suspend operator fun invoke(name: String) {
        productSuggestionRepository.addSuggestion(name)
    }
}
