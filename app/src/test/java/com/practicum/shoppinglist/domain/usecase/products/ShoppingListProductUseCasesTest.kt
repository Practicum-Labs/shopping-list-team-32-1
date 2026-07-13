package com.practicum.shoppinglist.domain.usecase.products

import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.FakeShoppingListRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingListProductUseCasesTest {

    private val shoppingListRepository = FakeShoppingListRepository()

    @Test
    fun `получает список покупок по id`() = runTest {
        val useCase = GetShoppingListUseCase(shoppingListRepository)
        val shoppingList = shoppingList()
        shoppingListRepository.shoppingListById = shoppingList

        val result = useCase(LIST_ID)

        assertEquals(shoppingList, result)
    }

    @Test
    fun `обновляет тип сортировки списка покупок`() = runTest {
        val useCase = UpdateShoppingListSortTypeUseCase(shoppingListRepository)

        useCase(shoppingListId = LIST_ID, sortType = SORT_TYPE)

        assertEquals(
            FakeShoppingListRepository.UpdatedSortType(
                shoppingListId = LIST_ID,
                sortType = SORT_TYPE,
            ),
            shoppingListRepository.updatedSortTypes.single(),
        )
    }

    private fun shoppingList(): ShoppingList {
        return ShoppingList(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
        )
    }

    private companion object {
        const val LIST_ID = 1L
        const val LIST_NAME = "Продукты"
        const val ICON_NAME = "cart"
        const val SORT_TYPE = "Alphabetical"
    }
}
