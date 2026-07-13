package com.practicum.shoppinglist.domain.usecase

import app.cash.turbine.test
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.products.FakeShoppingItemRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingListUseCasesTest {

    private val shoppingListRepository = FakeShoppingListRepository()

    @Test
    fun `создает список покупок и возвращает его id`() = runTest {
        val useCase = CreateShoppingListUseCase(shoppingListRepository)

        val result = useCase(name = LIST_NAME, iconName = ICON_NAME)

        assertEquals(CREATED_LIST_ID, result)
        assertEquals(
            FakeShoppingListRepository.CreatedShoppingList(name = LIST_NAME, iconName = ICON_NAME),
            shoppingListRepository.createdLists.single(),
        )
    }

    @Test
    fun `наблюдает за списками покупок из репозитория`() = runTest {
        val useCase = ObserveShoppingListsUseCase(shoppingListRepository)
        val lists = listOf(shoppingList())

        useCase().test {
            shoppingListRepository.shoppingListsFlow.emit(lists)

            assertEquals(lists, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `удаляет все списки покупок`() = runTest {
        val useCase = DeleteAllShoppingListsUseCase(shoppingListRepository)

        useCase()

        assertEquals(1, shoppingListRepository.deleteAllCallCount)
    }

    @Test
    fun `удаляет список покупок по id`() = runTest {
        val useCase = DeleteShoppingListUseCase(shoppingListRepository)

        useCase(LIST_ID)

        assertEquals(LIST_ID, shoppingListRepository.deletedListIds.single())
    }

    @Test
    fun `переименовывает список покупок`() = runTest {
        val useCase = RenameShoppingListUseCase(shoppingListRepository)

        useCase(shoppingListId = LIST_ID, name = RENAMED_LIST_NAME)

        assertEquals(
            FakeShoppingListRepository.RenamedShoppingList(
                shoppingListId = LIST_ID,
                name = RENAMED_LIST_NAME,
            ),
            shoppingListRepository.renamedLists.single(),
        )
    }

    @Test
    fun `обновляет иконку списка покупок`() = runTest {
        val useCase = UpdateShoppingListIconUseCase(shoppingListRepository)

        useCase(shoppingListId = LIST_ID, iconName = NEW_ICON_NAME)

        assertEquals(
            FakeShoppingListRepository.UpdatedIcon(
                shoppingListId = LIST_ID,
                iconName = NEW_ICON_NAME,
            ),
            shoppingListRepository.updatedIcons.single(),
        )
    }

    @Test
    fun `копирует список покупок вместе с товарами`() = runTest {
        val shoppingItemRepository = FakeShoppingItemRepository()
        shoppingItemRepository.itemsForList = listOf(shoppingItem(id = ITEM_ID, listId = LIST_ID))
        val useCase = CopyShoppingListUseCase(
            shoppingListRepository = shoppingListRepository,
            shoppingItemRepository = shoppingItemRepository,
        )

        val result = useCase(
            originalListId = LIST_ID,
            copiedName = COPIED_LIST_NAME,
            iconName = ICON_NAME,
        )

        assertEquals(CREATED_LIST_ID, result)
        assertEquals(LIST_ID, shoppingItemRepository.requestedListIds.single())
        assertEquals(
            shoppingItem(id = 0L, listId = CREATED_LIST_ID),
            shoppingItemRepository.insertedItemLists.single().single(),
        )
    }

    private fun shoppingList(): ShoppingList {
        return ShoppingList(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
        )
    }

    private fun shoppingItem(id: Long, listId: Long): ShoppingItem {
        return ShoppingItem(
            id = id,
            listId = listId,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
        )
    }

    private companion object {
        const val LIST_ID = 1L
        const val CREATED_LIST_ID = 10L
        const val ITEM_ID = 20L
        const val LIST_NAME = "Продукты"
        const val RENAMED_LIST_NAME = "Дом"
        const val COPIED_LIST_NAME = "Продукты копия"
        const val ICON_NAME = "cart"
        const val NEW_ICON_NAME = "home"
        const val ITEM_NAME = "Молоко"
        const val ITEM_QUANTITY = 1.0
        const val ITEM_UNIT = "л"
    }
}
