package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AddShoppingItemUseCaseTest {

    private val shoppingItemRepository = FakeShoppingItemRepository()
    private val productSuggestionRepository = FakeProductSuggestionRepository()
    private val useCase = AddShoppingItemUseCase(
        shoppingItemRepository = shoppingItemRepository,
        productSuggestionRepository = productSuggestionRepository,
    )

    @Test
    fun `обрезает пробелы в названии товара и сохраняет подсказку`() = runTest {
        useCase(
            listId = LIST_ID,
            name = "  $ITEM_NAME  ",
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            currentItems = emptyList(),
        )

        assertEquals(ITEM_NAME, productSuggestionRepository.addedSuggestions.single())
        assertEquals(ITEM_NAME, shoppingItemRepository.insertedItems.single().name)
    }

    @Test
    fun `добавляет товар перед первым текущим товаром`() = runTest {
        val currentItems = listOf(
            shoppingItem(sortOrder = 5),
            shoppingItem(sortOrder = 2),
            shoppingItem(sortOrder = 8),
        )

        useCase(
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            currentItems = currentItems,
        )

        assertEquals(1, shoppingItemRepository.insertedItems.single().sortOrder)
    }

    private fun shoppingItem(sortOrder: Int): ShoppingItem {
        return ShoppingItem(
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            sortOrder = sortOrder,
        )
    }

    private companion object {
        const val LIST_ID = 1L
        const val ITEM_NAME = "Молоко"
        const val ITEM_QUANTITY = 1.0
        const val ITEM_UNIT = "л"
    }
}
