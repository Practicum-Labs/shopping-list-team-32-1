package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow

class GetProductSuggestionsUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    operator fun invoke(query: String): Flow<List<String>> {
        return shoppingItemRepository.getSuggestionsFlow(query)
    }
}
