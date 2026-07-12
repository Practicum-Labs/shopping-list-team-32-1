package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.repository.ProductSuggestionRepository
import kotlinx.coroutines.flow.Flow

class GetProductSuggestionsUseCase(
    private val productSuggestionRepository: ProductSuggestionRepository
) {
    operator fun invoke(query: String): Flow<List<String>> {
        return productSuggestionRepository.getSuggestionsFlow(query)
    }
}
