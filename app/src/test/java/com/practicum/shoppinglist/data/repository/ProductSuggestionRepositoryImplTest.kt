package com.practicum.shoppinglist.data.repository

import app.cash.turbine.test
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductSuggestionRepositoryImplTest {

    private val shoppingItemDao = FakeShoppingItemDao()
    private val repository = ProductSuggestionRepositoryImpl(shoppingItemDao)

    @Test
    fun `возвращает только названия подсказок`() = runTest {
        val suggestions = listOf(
            ProductSuggestionEntity(name = FIRST_PRODUCT),
            ProductSuggestionEntity(name = SECOND_PRODUCT),
        )

        repository.getSuggestionsFlow(QUERY).test {
            shoppingItemDao.suggestionsFlow.emit(suggestions)

            assertEquals(listOf(FIRST_PRODUCT, SECOND_PRODUCT), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `сохраняет подсказку без лишних пробелов`() = runTest {
        repository.addSuggestion("  $FIRST_PRODUCT  ")

        assertEquals(ProductSuggestionEntity(FIRST_PRODUCT), shoppingItemDao.insertedSuggestions.single())
    }

    @Test
    fun `не сохраняет пустую подсказку`() = runTest {
        repository.addSuggestion("   ")

        assertTrue(shoppingItemDao.insertedSuggestions.isEmpty())
    }

    private companion object {
        const val QUERY = "мо"
        const val FIRST_PRODUCT = "Молоко"
        const val SECOND_PRODUCT = "Морковь"
    }
}
