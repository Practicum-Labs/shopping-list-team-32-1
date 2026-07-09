package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow

class ObserveShoppingItemsUseCase(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    operator fun invoke(listId: Long): Flow<List<ShoppingItem>> {
        return shoppingItemRepository.getItemsForListFlow(listId)
    }
}
