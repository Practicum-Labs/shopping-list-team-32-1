package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdateShoppingItemUseCaseTest {

    private val shoppingItemRepository = FakeShoppingItemRepository()
    private val productSuggestionRepository = FakeProductSuggestionRepository()
    private val useCase = UpdateShoppingItemUseCase(
        shoppingItemRepository = shoppingItemRepository,
        productSuggestionRepository = productSuggestionRepository,
    )

    @Test
    fun `обрезает пробелы в названии и сохраняет подсказку`() = runTest {
        useCase(
            item = originalItem(),
            name = "  $UPDATED_NAME  ",
            quantity = UPDATED_QUANTITY,
            unit = UPDATED_UNIT,
        )

        assertEquals(UPDATED_NAME, productSuggestionRepository.addedSuggestions.single())
        assertEquals(UPDATED_NAME, shoppingItemRepository.updatedItems.single().name)
    }

    @Test
    fun `сохраняет идентификаторы товара и обновляет редактируемые поля`() = runTest {
        useCase(
            item = originalItem(),
            name = UPDATED_NAME,
            quantity = UPDATED_QUANTITY,
            unit = UPDATED_UNIT,
        )

        assertEquals(
            originalItem().copy(
                name = UPDATED_NAME,
                quantity = UPDATED_QUANTITY,
                unit = UPDATED_UNIT,
            ),
            shoppingItemRepository.updatedItems.single(),
        )
    }

    private fun originalItem(): ShoppingItem {
        return ShoppingItem(
            id = ITEM_ID,
            listId = LIST_ID,
            name = ORIGINAL_NAME,
            quantity = ORIGINAL_QUANTITY,
            unit = ORIGINAL_UNIT,
            isBought = true,
            sortOrder = SORT_ORDER,
        )
    }

    private companion object {
        const val ITEM_ID = 10L
        const val LIST_ID = 20L
        const val ORIGINAL_NAME = "Хлеб"
        const val ORIGINAL_QUANTITY = 1.0
        const val ORIGINAL_UNIT = "шт"
        const val UPDATED_NAME = "Молоко"
        const val UPDATED_QUANTITY = 2.0
        const val UPDATED_UNIT = "л"
        const val SORT_ORDER = 7
    }
}
