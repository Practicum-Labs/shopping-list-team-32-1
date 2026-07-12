package com.practicum.shoppinglist.presentation.ui.common.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.common.ShoppingListMenuContent
import com.practicum.shoppinglist.presentation.ui.common.SortType

@Preview(name = "Menu Sheet - Base", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetBasePreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                onRenameClick = {},
            )
        }
    }
}

@Preview(name = "Menu Sheet - Base (Dark)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetBaseDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                onRenameClick = {},
            )
        }
    }
}

@Preview(name = "Menu Sheet - Sort Open (Alphabetical)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortAlphabeticalPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                onRenameClick = {},
                initialSortExpanded = true,
            )
        }
    }
}

@Preview(name = "Menu Sheet - Sort Open (Alphabetical) (Dark)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortAlphabeticalDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                onRenameClick = {},
                initialSortExpanded = true,
            )
        }
    }
}

@Preview(name = "Menu Sheet - Sort Open (Custom)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortCustomPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Custom,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                onRenameClick = {},
                initialSortExpanded = true,
            )
        }
    }
}

@Preview(name = "Menu Sheet - Sort Open (Custom) (Dark)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortCustomDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Custom,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                onRenameClick = {},
                initialSortExpanded = true,
            )
        }
    }
}

@Preview(name = "Menu Sheet - Products (Single-action Sort)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetProductsPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                sortLabel = "Сортировать по алфавиту",
                onSortClick = {},
                onRenameClick = {},
            )
        }
    }
}

@Preview(name = "Menu Sheet - Products (Single-action Sort) (Dark)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetProductsDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                sortLabel = "Сортировать по алфавиту",
                onSortClick = {},
                onRenameClick = {},
            )
        }
    }
}
