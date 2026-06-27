package com.practicum.shoppinglist.presentation.ui.main

data class MainScreenActions(
    val onSearchClick: () -> Unit = {},
    val onDeleteClick: () -> Unit = {},
    val onThemeClick: () -> Unit = {},
    val onAddClick: () -> Unit = {},
    val onAddListDismiss: () -> Unit = {},
    val onNewListNameChange: (String) -> Unit = {},
    val onCreateListClick: () -> Unit = {},
    val onIconPickerDismiss: () -> Unit = {},
    val onIconSelected: (String) -> Unit = {},
    val onShoppingListScrollHandled: () -> Unit = {},
    val onRetryClick: () -> Unit = {},
)
