package com.practicum.shoppinglist.presentation.ui.main

import com.practicum.shoppinglist.domain.model.ShoppingList

data class MainUiState(
    val contentState: MainContentState = MainContentState.Loading,
    val isAddListDialogVisible: Boolean = false,
    val newListName: String = "",
    val isCreatingList: Boolean = false,
    val iconPickerState: IconPickerState = IconPickerState.Hidden,
    val scrollToShoppingListId: Long? = null,
)

sealed interface MainContentState {
    data object Loading : MainContentState

    data object Empty : MainContentState

    data object Error : MainContentState

    data class Content(
        val shoppingLists: List<ShoppingList>,
    ) : MainContentState
}

sealed interface IconPickerState {
    data object Hidden : IconPickerState

    data class Visible(
        val shoppingListId: Long,
        val isUpdating: Boolean = false,
        val isErrorVisible: Boolean = false,
    ) : IconPickerState
}
