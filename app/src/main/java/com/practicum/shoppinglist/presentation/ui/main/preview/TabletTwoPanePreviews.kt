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
import com.practicum.shoppinglist.presentation.ui.main.MainContentState
import com.practicum.shoppinglist.presentation.ui.main.MainScreen
import com.practicum.shoppinglist.presentation.ui.main.MainUiState
import com.practicum.shoppinglist.presentation.ui.common.SortType
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
private fun TabletTwoPaneContent() {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            MainScreen(
                uiState = MainUiState(
                    contentState = MainContentState.Content(shoppingLists = tabletShoppingLists),
                ),
            )
        }
        VerticalDivider(modifier = Modifier.fillMaxHeight())
        Box(modifier = Modifier.weight(1.4f)) {
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
