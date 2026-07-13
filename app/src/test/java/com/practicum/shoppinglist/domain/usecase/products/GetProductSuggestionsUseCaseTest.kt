package com.practicum.shoppinglist.domain.usecase.products

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetProductSuggestionsUseCaseTest {

    private val productSuggestionRepository = FakeProductSuggestionRepository()
    private val useCase = GetProductSuggestionsUseCase(productSuggestionRepository)

    @Test
    fun `запрашивает подсказки по введенному тексту`() = runTest {
        val suggestions = listOf(FIRST_SUGGESTION, SECOND_SUGGESTION)

        useCase(QUERY).test {
            productSuggestionRepository.suggestionsFlow.emit(suggestions)

            assertEquals(QUERY, productSuggestionRepository.requestedQueries.single())
            assertEquals(suggestions, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        const val QUERY = "мо"
        const val FIRST_SUGGESTION = "Молоко"
        const val SECOND_SUGGESTION = "Морковь"
    }
}
