package com.practicum.shoppinglist.presentation.ui.products.preview

import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList

internal val previewShoppingList = ShoppingList(id = 1L, name = "Продукты на выходные", iconName = "list_alt")

internal val previewItems = listOf(
    ShoppingItem(id = 1L, listId = 1L, name = "Молоко", quantity = 1.0, unit = "л.", isBought = false, sortOrder = 0),
    ShoppingItem(id = 2L, listId = 1L, name = "Хлеб", quantity = 2.0, unit = "шт.", isBought = true, sortOrder = 1),
    ShoppingItem(id = 3L, listId = 1L, name = "Яблоки", quantity = 1.5, unit = "кг", isBought = false, sortOrder = 2),
)
