package com.practicum.shoppinglist.presentation.ui.products

import app.cash.turbine.test
import com.practicum.shoppinglist.MainDispatcherRule
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.FakeShoppingListRepository
import com.practicum.shoppinglist.domain.usecase.RenameShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.products.AddShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.ClearBoughtItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.CommitShoppingItemOrderUseCase
import com.practicum.shoppinglist.domain.usecase.products.DeleteAllShoppingItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.DeleteShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.FakeProductSuggestionRepository
import com.practicum.shoppinglist.domain.usecase.products.FakeShoppingItemRepository
import com.practicum.shoppinglist.domain.usecase.products.GetProductSuggestionsUseCase
import com.practicum.shoppinglist.domain.usecase.products.GetShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.products.ObserveShoppingItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.ToggleShoppingItemBoughtUseCase
import com.practicum.shoppinglist.domain.usecase.products.UpdateShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.UpdateShoppingListSortTypeUseCase
import com.practicum.shoppinglist.presentation.ui.common.SortType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val shoppingListRepository = FakeShoppingListRepository()
    private val shoppingItemRepository = FakeShoppingItemRepository()
    private val productSuggestionRepository = FakeProductSuggestionRepository()

    @Test
    fun `показывает что список удален если список не найден`() = runTest {
        shoppingListRepository.shoppingListById = null

        val viewModel = createViewModel()

        assertEquals(true, viewModel.uiState.value.listDeleted)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `загружает список и товары`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        shoppingItemRepository.itemsForList = listOf(shoppingItem(name = SECOND_ITEM), shoppingItem(name = FIRST_ITEM))

        val viewModel = createViewModel()

        assertEquals(shoppingList(), viewModel.uiState.value.list)
        assertEquals(
            listOf(shoppingItem(name = SECOND_ITEM), shoppingItem(name = FIRST_ITEM)),
            viewModel.uiState.value.items,
        )
    }

    @Test
    fun `сортирует товары по алфавиту`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        shoppingItemRepository.itemsForList = listOf(shoppingItem(name = SECOND_ITEM), shoppingItem(name = FIRST_ITEM))
        val viewModel = createViewModel()

        viewModel.selectSortType(SortType.Alphabetical)

        assertEquals(
            listOf(shoppingItem(name = FIRST_ITEM), shoppingItem(name = SECOND_ITEM)),
            viewModel.uiState.value.items,
        )
        assertEquals(
            FakeShoppingListRepository.UpdatedSortType(LIST_ID, SortType.Alphabetical.name),
            shoppingListRepository.updatedSortTypes.single(),
        )
    }

    @Test
    fun `добавляет товар в текущий список`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()

        viewModel.addProduct(name = "  $FIRST_ITEM  ", quantity = ITEM_QUANTITY, unit = ITEM_UNIT)

        assertEquals(FIRST_ITEM, shoppingItemRepository.insertedItems.single().name)
        assertEquals(FIRST_ITEM, productSuggestionRepository.addedSuggestions.single())
    }

    @Test
    fun `переименовывает текущий список`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()

        viewModel.renameList("  $RENAMED_LIST_NAME  ")

        assertEquals(
            FakeShoppingListRepository.RenamedShoppingList(LIST_ID, RENAMED_LIST_NAME),
            shoppingListRepository.renamedLists.single(),
        )
        assertEquals(RENAMED_LIST_NAME, viewModel.uiState.value.list?.name)
    }

    @Test
    fun `удаляет выбранный товар`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()
        val item = shoppingItem(name = FIRST_ITEM)

        viewModel.deleteProduct(item)

        assertEquals(item, shoppingItemRepository.deletedItems.single())
    }

    @Test
    fun `обновляет выбранный товар`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()
        val item = shoppingItem(name = FIRST_ITEM)

        viewModel.updateProduct(
            item = item,
            name = "  $UPDATED_ITEM  ",
            quantity = UPDATED_QUANTITY,
            unit = UPDATED_UNIT,
        )

        assertEquals(
            item.copy(name = UPDATED_ITEM, quantity = UPDATED_QUANTITY, unit = UPDATED_UNIT),
            shoppingItemRepository.updatedItems.single(),
        )
        assertEquals(UPDATED_ITEM, productSuggestionRepository.addedSuggestions.single())
    }

    @Test
    fun `переключает отметку что товар куплен`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()
        val item = shoppingItem(name = FIRST_ITEM)

        viewModel.toggleProductBought(item)

        assertEquals(item.copy(isBought = true), shoppingItemRepository.updatedItems.single())
    }

    @Test
    fun `удаляет все товары текущего списка`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()

        viewModel.deleteAllItems()

        assertEquals(LIST_ID, shoppingItemRepository.deletedAllListIds.single())
    }

    @Test
    fun `очищает купленные товары текущего списка`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()

        viewModel.clearBoughtItems()

        assertEquals(LIST_ID, shoppingItemRepository.clearedBoughtListIds.single())
    }

    @Test
    fun `перемещает товар и переключает сортировку на пользовательскую`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        shoppingItemRepository.itemsForList = listOf(
            shoppingItem(id = FIRST_ITEM_ID, name = FIRST_ITEM),
            shoppingItem(id = SECOND_ITEM_ID, name = SECOND_ITEM),
        )
        val viewModel = createViewModel()

        viewModel.reorderItems(fromIndex = FIRST_INDEX, toIndex = SECOND_INDEX)

        assertEquals(
            listOf(
                shoppingItem(id = SECOND_ITEM_ID, name = SECOND_ITEM, sortOrder = FIRST_INDEX),
                shoppingItem(id = FIRST_ITEM_ID, name = FIRST_ITEM, sortOrder = SECOND_INDEX),
            ),
            viewModel.uiState.value.items,
        )
        assertEquals(SortType.Custom, viewModel.uiState.value.sortType)
    }

    @Test
    fun `сохраняет ручной порядок товаров`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        shoppingItemRepository.itemsForList = listOf(
            shoppingItem(id = FIRST_ITEM_ID, name = FIRST_ITEM),
            shoppingItem(id = SECOND_ITEM_ID, name = SECOND_ITEM),
        )
        val viewModel = createViewModel()
        viewModel.reorderItems(fromIndex = FIRST_INDEX, toIndex = SECOND_INDEX)

        viewModel.commitItemOrder()

        assertEquals(viewModel.uiState.value.items, shoppingItemRepository.updatedItemLists.single())
        assertEquals(
            FakeShoppingListRepository.UpdatedSortType(LIST_ID, SortType.Custom.name),
            shoppingListRepository.updatedSortTypes.single(),
        )
    }

    @Test
    fun `запрашивает подсказки по введенному товару`() = runTest {
        shoppingListRepository.shoppingListById = shoppingList()
        val viewModel = createViewModel()

        viewModel.suggestions.test {
            assertEquals(emptyList<String>(), awaitItem())

            viewModel.updateSuggestionQuery(SUGGESTION_QUERY)
            productSuggestionRepository.suggestionsFlow.emit(listOf(FIRST_ITEM))

            assertEquals(SUGGESTION_QUERY, productSuggestionRepository.requestedQueries.single())
            assertEquals(listOf(FIRST_ITEM), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(): ProductsViewModel {
        return ProductsViewModel(
            listId = LIST_ID,
            renameShoppingListUseCase = RenameShoppingListUseCase(shoppingListRepository),
            deleteAllShoppingItemsUseCase = DeleteAllShoppingItemsUseCase(shoppingItemRepository),
            getShoppingListUseCase = GetShoppingListUseCase(shoppingListRepository),
            observeShoppingItemsUseCase = ObserveShoppingItemsUseCase(shoppingItemRepository),
            addShoppingItemUseCase = AddShoppingItemUseCase(shoppingItemRepository, productSuggestionRepository),
            updateShoppingItemUseCase = UpdateShoppingItemUseCase(shoppingItemRepository, productSuggestionRepository),
            deleteShoppingItemUseCase = DeleteShoppingItemUseCase(shoppingItemRepository),
            toggleShoppingItemBoughtUseCase = ToggleShoppingItemBoughtUseCase(shoppingItemRepository),
            clearBoughtItemsUseCase = ClearBoughtItemsUseCase(shoppingItemRepository),
            commitShoppingItemOrderUseCase = CommitShoppingItemOrderUseCase(shoppingItemRepository),
            getProductSuggestionsUseCase = GetProductSuggestionsUseCase(productSuggestionRepository),
            updateShoppingListSortTypeUseCase = UpdateShoppingListSortTypeUseCase(shoppingListRepository),
        )
    }

    private fun shoppingList(): ShoppingList {
        return ShoppingList(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
        )
    }

    private fun shoppingItem(
        id: Long = ITEM_ID,
        name: String,
        sortOrder: Int = DEFAULT_SORT_ORDER,
    ): ShoppingItem {
        return ShoppingItem(
            id = id,
            listId = LIST_ID,
            name = name,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            sortOrder = sortOrder,
        )
    }

    private companion object {
        const val LIST_ID = 1L
        const val ITEM_ID = 10L
        const val FIRST_ITEM_ID = 11L
        const val SECOND_ITEM_ID = 12L
        const val LIST_NAME = "Продукты"
        const val RENAMED_LIST_NAME = "Дом"
        const val ICON_NAME = "cart"
        const val FIRST_ITEM = "Апельсины"
        const val SECOND_ITEM = "Яблоки"
        const val UPDATED_ITEM = "Молоко"
        const val SUGGESTION_QUERY = "ап"
        const val ITEM_QUANTITY = 1.0
        const val UPDATED_QUANTITY = 2.0
        const val ITEM_UNIT = "кг"
        const val UPDATED_UNIT = "л"
        const val DEFAULT_SORT_ORDER = 0
        const val FIRST_INDEX = 0
        const val SECOND_INDEX = 1
    }
}
