package com.practicum.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProductSuggestionRepository {
    fun getSuggestionsFlow(query: String): Flow<List<String>>
    suspend fun addSuggestion(name: String)
}
