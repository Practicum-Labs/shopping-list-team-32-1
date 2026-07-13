package com.practicum.shoppinglist.domain.usecase.products

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DeleteAllShoppingItemsUseCaseTest {

    private val shoppingItemRepository = FakeShoppingItemRepository()
    private val useCase = DeleteAllShoppingItemsUseCase(shoppingItemRepository)

    @Test
    fun `удаляет все товары только из переданного списка`() = runTest {
        useCase(LIST_ID)

        assertEquals(LIST_ID, shoppingItemRepository.deletedAllListIds.single())
    }

    private companion object {
        const val LIST_ID = 12L
    }
}
