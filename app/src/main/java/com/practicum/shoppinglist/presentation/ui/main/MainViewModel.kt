package com.practicum.shoppinglist.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.CreateShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.ObserveShoppingListsUseCase
import com.practicum.shoppinglist.domain.usecase.UpdateShoppingListIconUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(
    observeShoppingListsUseCase: ObserveShoppingListsUseCase,
    private val createShoppingListUseCase: CreateShoppingListUseCase,
    private val updateShoppingListIconUseCase: UpdateShoppingListIconUseCase,
) : ViewModel() {
    private val screenState = MutableStateFlow(MainUiState())
    private val retryRequests = MutableStateFlow(0)

    val uiState: StateFlow<MainUiState> = combine(
        retryRequests.flatMapLatest {
            observeShoppingListsUseCase()
                .map<List<ShoppingList>, MainContentState> { shoppingLists ->
                    if (shoppingLists.isEmpty()) {
                        MainContentState.Empty
                    } else {
                        MainContentState.Content(shoppingLists = shoppingLists)
                    }
                }
                .onStart { emit(MainContentState.Loading) }
                .catch { emit(MainContentState.Error) }
        },
        screenState,
    ) { contentState, currentState ->
        currentState.copy(contentState = contentState)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = MainUiState(),
        )

    fun onAddListClick() {
        screenState.update { currentState ->
            currentState.copy(
                isAddListDialogVisible = true,
                newListName = "",
                isCreatingList = false,
                iconPickerState = currentState.iconPickerState.hideError(),
            )
        }
    }

    fun onAddListDismiss() {
        if (screenState.value.isCreatingList) {
            return
        }

        screenState.update { currentState ->
            currentState.copy(
                isAddListDialogVisible = false,
                newListName = "",
                isCreatingList = false,
            )
        }
    }

    fun onNewListNameChange(newListName: String) {
        screenState.update { currentState ->
            currentState.copy(newListName = newListName.take(MAX_LIST_NAME_LENGTH))
        }
    }

    fun retryShoppingListsLoading() {
        retryRequests.update { retryRequest -> retryRequest + 1 }
    }

    fun createShoppingList() {
        val currentState = screenState.value
        val listName = currentState.newListName.trim()
        if (listName.isEmpty() || currentState.isCreatingList) {
            return
        }

        screenState.update { state ->
            state.copy(isCreatingList = true)
        }

        viewModelScope.launch {
            runCatching {
                createShoppingListUseCase(
                    name = listName,
                    iconName = DEFAULT_LIST_ICON_NAME,
                )
            }.onSuccess { createdListId ->
                screenState.update { state ->
                    state.copy(
                        isAddListDialogVisible = false,
                        newListName = "",
                        isCreatingList = false,
                        scrollToShoppingListId = createdListId,
                    )
                }
            }.onFailure {
                screenState.update { state ->
                    state.copy(
                        isCreatingList = false,
                    )
                }
            }
        }
    }

    fun onShoppingListIconClick(shoppingListId: Long) {
        if (screenState.value.iconPickerState.isUpdating) {
            return
        }

        screenState.update { currentState ->
            currentState.copy(
                iconPickerState = IconPickerState.Visible(
                    shoppingListId = shoppingListId,
                ),
            )
        }
    }

    fun onIconPickerDismiss() {
        if (screenState.value.iconPickerState.isUpdating) {
            return
        }

        screenState.update { currentState ->
            currentState.copy(
                iconPickerState = IconPickerState.Hidden,
            )
        }
    }

    fun onShoppingListIconSelected(iconName: String) {
        val currentState = screenState.value
        val iconPickerState = currentState.iconPickerState as? IconPickerState.Visible ?: return
        if (iconPickerState.isUpdating) {
            return
        }

        val shoppingListId = iconPickerState.shoppingListId
        screenState.update { state ->
            state.copy(
                iconPickerState = iconPickerState.copy(
                    isUpdating = true,
                    isErrorVisible = false,
                ),
            )
        }
        viewModelScope.launch {
            runCatching {
                updateShoppingListIconUseCase(
                    shoppingListId = shoppingListId,
                    iconName = iconName,
                )
            }.onSuccess {
                screenState.update { state ->
                    state.copy(
                        iconPickerState = IconPickerState.Hidden,
                    )
                }
            }.onFailure {
                screenState.update { state ->
                    state.copy(
                        iconPickerState = iconPickerState.copy(
                            isUpdating = false,
                            isErrorVisible = true,
                        ),
                    )
                }
            }
        }
    }

    fun onShoppingListScrollHandled() {
        screenState.update { currentState ->
            currentState.copy(scrollToShoppingListId = null)
        }
    }

    private companion object {
        const val MAX_LIST_NAME_LENGTH = 64
        const val DEFAULT_LIST_ICON_NAME = "list_alt"
    }
}

private val IconPickerState.isUpdating: Boolean
    get() = this is IconPickerState.Visible && isUpdating

private fun IconPickerState.hideError(): IconPickerState =
    if (this is IconPickerState.Visible) {
        copy(isErrorVisible = false)
    } else {
        this
    }
