package com.practicum.shoppinglist.presentation.ui.main.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.common.SortType
import com.practicum.shoppinglist.presentation.ui.main.MainContentState
import com.practicum.shoppinglist.presentation.ui.main.MainScreen
import com.practicum.shoppinglist.presentation.ui.main.MainUiState
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingScreen
import com.practicum.shoppinglist.presentation.ui.products.ProductsScreen
import com.practicum.shoppinglist.presentation.ui.products.ProductsUiState

private val tabletShoppingLists = listOf(
    ShoppingList(id = 1L, name = "Продукты на выходные", iconName = "list_alt"),
    ShoppingList(id = 2L, name = "Ремонт на кухне", iconName = "hardware"),
    ShoppingList(id = 3L, name = "День рождения", iconName = "cake"),
)

private val tabletProductItems = listOf(
    ShoppingItem(id = 1L, listId = 1L, name = "Молоко", quantity = 1.0, unit = "л.", isBought = false, sortOrder = 0),
    ShoppingItem(id = 2L, listId = 1L, name = "Хлеб", quantity = 2.0, unit = "шт.", isBought = true, sortOrder = 1),
    ShoppingItem(id = 3L, listId = 1L, name = "Яблоки", quantity = 1.5, unit = "кг", isBought = false, sortOrder = 2),
)

@Composable
private fun TabletTwoPaneContent(selectedListId: Long = tabletShoppingLists.first().id) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            MainScreen(
                uiState = MainUiState(
                    contentState = MainContentState.Content(shoppingLists = tabletShoppingLists),
                ),
                // The detail pane below shows this list's products, so it is the selected one.
                selectedListId = selectedListId,
            )
        }
        VerticalDivider(modifier = Modifier.fillMaxHeight())
        Box(modifier = Modifier.weight(DETAIL_PANE_WEIGHT)) {
            ProductsScreen(
                state = ProductsUiState(
                    list = tabletShoppingLists.first(),
                    items = tabletProductItems,
                    isLoading = false,
                    sortType = SortType.Custom,
                ),
                suggestions = emptyList(),
                onBack = {},
                onRenameList = {},
                onDeleteAllItems = {},
                onClearBought = {},
                onSortTypeSelected = {},
                onAddProduct = { _, _, _ -> },
                onUpdateProduct = { _, _, _, _ -> },
                onDeleteProduct = {},
                onToggleProductBought = {},
                onReorderItem = { _, _ -> },
                onCommitOrder = {},
                onUpdateSuggestionQuery = {},
            )
        }
    }
}

@Preview(
    name = "Tablet List-Detail (Light)",
    showBackground = true,
    widthDp = 840,
    heightDp = 1024,
)
@Composable
private fun TabletTwoPaneLightPreview() {
    Theme {
        TabletTwoPaneContent()
    }
}

@Preview(
    name = "Tablet List-Detail (Dark)",
    showBackground = true,
    widthDp = 840,
    heightDp = 1024,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TabletTwoPaneDarkPreview() {
    Theme(darkTheme = true) {
        TabletTwoPaneContent()
    }
}

@Composable
private fun TabletSearchListPaneContent() {
    MainScreen(
        uiState = MainUiState(
            contentState = MainContentState.Content(shoppingLists = tabletShoppingLists),
            isSearching = true,
            searchQuery = "н",
        ),
        selectedListId = tabletShoppingLists.first().id,
    )
}

@Preview(
    name = "Tablet List Pane, Search (Light)",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Composable
private fun TabletSearchListPaneLightPreview() {
    Theme {
        TabletSearchListPaneContent()
    }
}

@Preview(
    name = "Tablet List Pane, Search (Dark)",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TabletSearchListPaneDarkPreview() {
    Theme(darkTheme = true) {
        TabletSearchListPaneContent()
    }
}

@Preview(
    name = "Tablet Onboarding (Light)",
    showBackground = true,
    widthDp = 1280,
    heightDp = 800,
)
@Composable
private fun TabletOnboardingLightPreview() {
    Theme {
        OnboardingScreen()
    }
}

@Preview(
    name = "Tablet Onboarding (Dark)",
    showBackground = true,
    widthDp = 840,
    heightDp = 1024,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TabletOnboardingDarkPreview() {
    Theme(darkTheme = true) {
        OnboardingScreen(isDarkTheme = true)
    }
}

private const val DETAIL_PANE_WEIGHT = 1.4f
