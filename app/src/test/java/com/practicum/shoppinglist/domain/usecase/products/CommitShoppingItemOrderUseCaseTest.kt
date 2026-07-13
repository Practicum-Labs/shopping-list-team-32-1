package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CommitShoppingItemOrderUseCaseTest {

    private val shoppingItemRepository = FakeShoppingItemRepository()
    private val useCase = CommitShoppingItemOrderUseCase(shoppingItemRepository)

    @Test
    fun `передает новый порядок товаров в репозиторий`() = runTest {
        val items = listOf(
            shoppingItem(id = FIRST_ITEM_ID, sortOrder = SECOND_POSITION),
            shoppingItem(id = SECOND_ITEM_ID, sortOrder = FIRST_POSITION),
        )

        useCase(items)

        assertEquals(items, shoppingItemRepository.updatedItemLists.single())
    }

    private fun shoppingItem(id: Long, sortOrder: Int): ShoppingItem {
        return ShoppingItem(
            id = id,
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            sortOrder = sortOrder,
        )
    }

    private companion object {
        const val LIST_ID = 1L
        const val FIRST_ITEM_ID = 10L
        const val SECOND_ITEM_ID = 20L
        const val ITEM_NAME = "Молоко"
        const val ITEM_QUANTITY = 1.0
        const val ITEM_UNIT = "л"
        const val FIRST_POSITION = 1
        const val SECOND_POSITION = 2
    }
}
