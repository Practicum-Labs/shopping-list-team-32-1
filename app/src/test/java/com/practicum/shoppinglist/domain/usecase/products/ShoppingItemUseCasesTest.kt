package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingItemUseCasesTest {

    private val shoppingItemRepository = FakeShoppingItemRepository()

    @Test
    fun `очищает купленные товары в переданном списке`() = runTest {
        val useCase = ClearBoughtItemsUseCase(shoppingItemRepository)

        useCase(LIST_ID)

        assertEquals(LIST_ID, shoppingItemRepository.clearedBoughtListIds.single())
    }

    @Test
    fun `удаляет переданный товар`() = runTest {
        val useCase = DeleteShoppingItemUseCase(shoppingItemRepository)
        val item = shoppingItem()

        useCase(item)

        assertEquals(item, shoppingItemRepository.deletedItems.single())
    }

    @Test
    fun `получает товары для списка`() = runTest {
        val useCase = ObserveShoppingItemsUseCase(shoppingItemRepository)

        val result = useCase(LIST_ID)

        assertEquals(emptyList<ShoppingItem>(), result.first())
    }

    @Test
    fun `переключает отметку что товар куплен`() = runTest {
        val useCase = ToggleShoppingItemBoughtUseCase(shoppingItemRepository)
        val item = shoppingItem(isBought = false)

        useCase(item)

        assertEquals(item.copy(isBought = true), shoppingItemRepository.updatedItems.single())
    }

    private fun shoppingItem(isBought: Boolean = false): ShoppingItem {
        return ShoppingItem(
            id = ITEM_ID,
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            isBought = isBought,
        )
    }

    private companion object {
        const val ITEM_ID = 10L
        const val LIST_ID = 20L
        const val ITEM_NAME = "Молоко"
        const val ITEM_QUANTITY = 1.0
        const val ITEM_UNIT = "л"
    }
}
