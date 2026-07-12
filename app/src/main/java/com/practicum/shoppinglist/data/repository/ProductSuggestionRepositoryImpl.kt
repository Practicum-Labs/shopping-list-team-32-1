package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.domain.repository.ProductSuggestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductSuggestionRepositoryImpl(
    private val shoppingItemDao: ShoppingItemDao,
) : ProductSuggestionRepository {

    override fun getSuggestionsFlow(query: String): Flow<List<String>> {
        return shoppingItemDao.getSuggestionsFlow(query).map { list ->
            list.map { it.name }
        }
    }

    override suspend fun addSuggestion(name: String) {
        if (name.isNotBlank()) {
            shoppingItemDao.insertSuggestion(ProductSuggestionEntity(name = name.trim()))
        }
    }
}
