package com.practicum.shoppinglist.presentation.ui.products.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.common.SortType
import com.practicum.shoppinglist.presentation.ui.products.ProductsScreen
import com.practicum.shoppinglist.presentation.ui.products.ProductsUiState

@Composable
private fun ProductsScreenPreviewContent(
    darkTheme: Boolean,
    items: List<ShoppingItem>,
    sortType: SortType = SortType.Custom,
) {
    Theme(darkTheme = darkTheme) {
        ProductsScreen(
            state = ProductsUiState(
                list = previewShoppingList,
                items = items,
                isLoading = false,
                sortType = sortType
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
            onUpdateSuggestionQuery = {}
        )
    }
}

@Preview(name = "Products - With Items (Light)", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun ProductsScreenWithItemsLightPreview() {
    ProductsScreenPreviewContent(darkTheme = false, items = previewItems)
}

@Preview(
    name = "Products - With Items (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductsScreenWithItemsDarkPreview() {
    ProductsScreenPreviewContent(darkTheme = true, items = previewItems)
}

@Preview(name = "Products - Empty (Light)", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun ProductsScreenEmptyLightPreview() {
    ProductsScreenPreviewContent(darkTheme = false, items = emptyList())
}

@Preview(
    name = "Products - Empty (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductsScreenEmptyDarkPreview() {
    ProductsScreenPreviewContent(darkTheme = true, items = emptyList())
}
