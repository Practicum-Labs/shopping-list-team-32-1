package com.practicum.shoppinglist.presentation.ui.main

import app.cash.turbine.test
import com.practicum.shoppinglist.MainDispatcherRule
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.CopyShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.CreateShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.DeleteAllShoppingListsUseCase
import com.practicum.shoppinglist.domain.usecase.DeleteShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.FakeShoppingListRepository
import com.practicum.shoppinglist.domain.usecase.ObserveShoppingListsUseCase
import com.practicum.shoppinglist.domain.usecase.RenameShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.UpdateShoppingListIconUseCase
import com.practicum.shoppinglist.domain.usecase.auth.CheckAuthUseCase
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import com.practicum.shoppinglist.domain.usecase.auth.LogoutUseCase
import com.practicum.shoppinglist.domain.usecase.products.FakeShoppingItemRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val shoppingListRepository = FakeShoppingListRepository()
    private val shoppingItemRepository = FakeShoppingItemRepository()
    private val authRepository = FakeAuthRepository()

    @Test
    fun `показывает списки покупок из репозитория`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(MainContentState.Loading, awaitItem().contentState)

            shoppingListRepository.shoppingListsFlow.emit(listOf(shoppingList()))

            assertEquals(
                MainContentState.Content(listOf(shoppingList())),
                awaitItem().contentState,
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `фильтрует списки по поисковому запросу`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()
            shoppingListRepository.shoppingListsFlow.emit(
                listOf(
                    shoppingList(id = FIRST_LIST_ID, name = FIRST_LIST_NAME),
                    shoppingList(id = SECOND_LIST_ID, name = SECOND_LIST_NAME),
                ),
            )
            awaitItem()

            viewModel.onSearchClick()
            awaitItem()
            viewModel.onSearchQueryChange(SEARCH_QUERY)

            val contentState = awaitItem().contentState as MainContentState.Content
            assertEquals(listOf(shoppingList(id = SECOND_LIST_ID, name = SECOND_LIST_NAME)), contentState.shoppingLists)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `создает список с обрезанным названием`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAddListClick()
        viewModel.onNewListNameChange("  $FIRST_LIST_NAME  ")

        viewModel.createShoppingList()

        assertEquals(
            FakeShoppingListRepository.CreatedShoppingList(
                name = FIRST_LIST_NAME,
                iconName = DEFAULT_ICON_NAME,
            ),
            shoppingListRepository.createdLists.single(),
        )
        assertFalse(viewModel.uiState.value.isAddListDialogVisible)
    }

    @Test
    fun `закрывает диалог удаления всех списков после подтверждения`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDeleteAllClick()

        viewModel.onDeleteAllConfirm()

        assertEquals(1, shoppingListRepository.deleteAllCallCount)
        assertFalse(viewModel.uiState.value.isDeleteAllDialogVisible)
    }

    @Test
    fun `разлогинивает пользователя если фоновая проверка сессии не прошла`() = runTest {
        val viewModel = createViewModel()
        authRepository.checkSessionResult = false
        var isInvalidSessionHandled = false

        viewModel.checkSessionInBackground {
            isInvalidSessionHandled = true
        }

        assertEquals(1, authRepository.logoutCallCount)
        assertTrue(isInvalidSessionHandled)
    }

    @Test
    fun `не разлогинивает пользователя если фоновая проверка сессии прошла`() = runTest {
        val viewModel = createViewModel()
        authRepository.checkSessionResult = true
        var isInvalidSessionHandled = false

        viewModel.checkSessionInBackground {
            isInvalidSessionHandled = true
        }

        assertEquals(0, authRepository.logoutCallCount)
        assertFalse(isInvalidSessionHandled)
    }

    @Test
    fun `выполняет выход пользователя`() = runTest {
        val viewModel = createViewModel()
        var isSuccessCalled = false

        viewModel.onLogoutClick {
            isSuccessCalled = true
        }

        assertEquals(1, authRepository.logoutCallCount)
        assertTrue(isSuccessCalled)
    }

    @Test
    fun `открывает выбор иконки для списка`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onShoppingListIconClick(FIRST_LIST_ID)

            assertEquals(
                IconPickerState.Visible(shoppingListId = FIRST_LIST_ID),
                awaitItem().iconPickerState,
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `обновляет иконку выбранного списка`() = runTest {
        val viewModel = createViewModel()
        viewModel.onShoppingListIconClick(FIRST_LIST_ID)

        viewModel.onShoppingListIconSelected(SECOND_ICON_NAME)

        assertEquals(
            FakeShoppingListRepository.UpdatedIcon(
                shoppingListId = FIRST_LIST_ID,
                iconName = SECOND_ICON_NAME,
            ),
            shoppingListRepository.updatedIcons.single(),
        )
        assertEquals(IconPickerState.Hidden, viewModel.uiState.value.iconPickerState)
    }

    @Test
    fun `удаляет выбранный список после подтверждения`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()
            shoppingListRepository.shoppingListsFlow.emit(listOf(shoppingList()))
            awaitItem()

            viewModel.onDeleteListClick(FIRST_LIST_ID)
            assertEquals(shoppingList(), awaitItem().deleteListConfirmDialogTarget)

            viewModel.onDeleteListConfirm()

            assertEquals(FIRST_LIST_ID, shoppingListRepository.deletedListIds.single())
            assertEquals(null, awaitItem().deleteListConfirmDialogTarget)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `копирует выбранный список и запоминает куда проскроллить`() = runTest {
        val viewModel = createViewModel()
        shoppingListRepository.createdListId = CREATED_LIST_ID

        viewModel.uiState.test {
            awaitItem()
            shoppingListRepository.shoppingListsFlow.emit(listOf(shoppingList()))
            awaitItem()

            viewModel.onCopyListClick(
                shoppingListId = FIRST_LIST_ID,
                copiedName = COPIED_LIST_NAME,
            )

            assertEquals(CREATED_LIST_ID, awaitItem().scrollToShoppingListId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `переименовывает выбранный список после подтверждения`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()
            shoppingListRepository.shoppingListsFlow.emit(listOf(shoppingList()))
            awaitItem()

            viewModel.onRenameListClick(FIRST_LIST_ID)
            awaitItem()
            viewModel.onRenameListNameChange("  $RENAMED_LIST_NAME  ")
            awaitItem()
            viewModel.onRenameListConfirm()

            assertEquals(
                FakeShoppingListRepository.RenamedShoppingList(
                    shoppingListId = FIRST_LIST_ID,
                    name = RENAMED_LIST_NAME,
                ),
                shoppingListRepository.renamedLists.single(),
            )
            var finalState: MainUiState
            do {
                finalState = awaitItem()
            } while (finalState.renameListTarget != null || finalState.isRenamingList)
            assertEquals("", finalState.renameListName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(): MainViewModel {
        return MainViewModel(
            observeShoppingListsUseCase = ObserveShoppingListsUseCase(shoppingListRepository),
            createShoppingListUseCase = CreateShoppingListUseCase(shoppingListRepository),
            updateShoppingListIconUseCase = UpdateShoppingListIconUseCase(shoppingListRepository),
            deleteAllShoppingListsUseCase = DeleteAllShoppingListsUseCase(shoppingListRepository),
            deleteShoppingListUseCase = DeleteShoppingListUseCase(shoppingListRepository),
            copyShoppingListUseCase = CopyShoppingListUseCase(shoppingListRepository, shoppingItemRepository),
            renameShoppingListUseCase = RenameShoppingListUseCase(shoppingListRepository),
            checkAuthUseCase = CheckAuthUseCase(authRepository),
            logoutUseCase = LogoutUseCase(authRepository),
        )
    }

    private fun shoppingList(id: Long = FIRST_LIST_ID, name: String = FIRST_LIST_NAME): ShoppingList {
        return ShoppingList(
            id = id,
            name = name,
            iconName = DEFAULT_ICON_NAME,
        )
    }

    private companion object {
        const val FIRST_LIST_ID = 1L
        const val SECOND_LIST_ID = 2L
        const val CREATED_LIST_ID = 10L
        const val FIRST_LIST_NAME = "Продукты"
        const val SECOND_LIST_NAME = "Дом"
        const val RENAMED_LIST_NAME = "Покупки"
        const val COPIED_LIST_NAME = "Продукты копия"
        const val SEARCH_QUERY = "до"
        const val DEFAULT_ICON_NAME = "list_alt"
        const val SECOND_ICON_NAME = "home"
    }
}
