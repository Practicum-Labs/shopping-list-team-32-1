package com.practicum.shoppinglist.domain.usecase.products

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AddProductSuggestionUseCaseTest {

    private val productSuggestionRepository = FakeProductSuggestionRepository()
    private val useCase = AddProductSuggestionUseCase(productSuggestionRepository)

    @Test
    fun `сохраняет название товара как подсказку`() = runTest {
        useCase(PRODUCT_NAME)

        assertEquals(PRODUCT_NAME, productSuggestionRepository.addedSuggestions.single())
    }

    private companion object {
        const val PRODUCT_NAME = "Молоко"
    }
}
