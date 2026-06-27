package com.practicum.shoppinglist.presentation.ui.main

import com.practicum.shoppinglist.domain.model.ShoppingList

data class MainUiState(
    val contentState: MainContentState = MainContentState.Loading,
    val isAddListDialogVisible: Boolean = false,
    val newListName: String = "",
    val isCreatingList: Boolean = false,
    val isIconPickerVisible: Boolean = false,
    val isUpdatingIcon: Boolean = false,
    val isIconPickerErrorVisible: Boolean = false,
    val iconPickerShoppingListId: Long? = null,
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
