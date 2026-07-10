package com.practicum.shoppinglist.presentation.ui.products.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.main.SortType
import com.practicum.shoppinglist.presentation.ui.products.BottomSheetContent
import com.practicum.shoppinglist.presentation.ui.products.CancelConfirmDialog
import com.practicum.shoppinglist.presentation.ui.products.ClearBoughtConfirmDialog
import com.practicum.shoppinglist.presentation.ui.products.DeleteConfirmDialog
import com.practicum.shoppinglist.presentation.ui.products.ProductItemRow
import com.practicum.shoppinglist.presentation.ui.products.ProductSwipeBackground
import com.practicum.shoppinglist.presentation.ui.products.ProductsScreen
import com.practicum.shoppinglist.presentation.ui.products.ProductsUiState
import com.practicum.shoppinglist.presentation.ui.products.RenameListDialog

private val previewShoppingList = ShoppingList(id = 1L, name = "Продукты на выходные", iconName = "list_alt")

private val previewItems = listOf(
    ShoppingItem(id = 1L, listId = 1L, name = "Молоко", quantity = 1.0, unit = "л.", isBought = false, sortOrder = 0),
    ShoppingItem(id = 2L, listId = 1L, name = "Хлеб", quantity = 2.0, unit = "шт.", isBought = true, sortOrder = 1),
    ShoppingItem(id = 3L, listId = 1L, name = "Яблоки", quantity = 1.5, unit = "кг", isBought = false, sortOrder = 2),
)

@Composable
private fun ProductsScreenPreviewContent(
    items: List<ShoppingItem>,
    sortType: SortType = SortType.Custom
) {
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
        onDeleteList = {},
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

@Preview(name = "Products - With Items (Light)", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun ProductsScreenWithItemsLightPreview() {
    Theme {
        ProductsScreenPreviewContent(items = previewItems)
    }
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
    Theme(darkTheme = true) {
        ProductsScreenPreviewContent(items = previewItems)
    }
}

@Preview(name = "Products - Empty (Light)", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun ProductsScreenEmptyLightPreview() {
    Theme {
        ProductsScreenPreviewContent(items = emptyList())
    }
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
    Theme(darkTheme = true) {
        ProductsScreenPreviewContent(items = emptyList())
    }
}

@Preview(name = "Products - Sorted Alphabetically (Light)", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun ProductsScreenAlphabeticalSortLightPreview() {
    Theme {
        ProductsScreenPreviewContent(items = previewItems, sortType = SortType.Alphabetical)
    }
}

@Composable
private fun ProductItemStatesPreviewContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            ProductItemRow(item = previewItems[0], onToggleBought = {})
            ProductItemRow(item = previewItems[1], onToggleBought = {})
            ProductItemRow(item = previewItems[2], onToggleBought = {}, isDragging = true)
            Box(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                ProductSwipeBackground(isLongSwipe = false, onEditClick = {}, onDeleteClick = {})
            }
            Box(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                ProductSwipeBackground(isLongSwipe = true, onEditClick = {}, onDeleteClick = {})
            }
        }
    }
}

@Preview(name = "Product Item States (Light)", showBackground = true, widthDp = 428, heightDp = 500)
@Composable
private fun ProductItemStatesLightPreview() {
    Theme {
        ProductItemStatesPreviewContent()
    }
}

@Preview(
    name = "Product Item States (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 500,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductItemStatesDarkPreview() {
    Theme(darkTheme = true) {
        ProductItemStatesPreviewContent()
    }
}

@Composable
private fun BottomSheetContentPreviewContent(isEditing: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .height(360.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomSheetContent(
            name = if (isEditing) "Йогурт" else "",
            onNameChange = {},
            qtyStr = if (isEditing) "4" else "",
            onQtyChange = {},
            unit = if (isEditing) "шт." else "",
            onUnitChange = {},
            suggestions = emptyList(),
            onQueryChange = {},
            onSaveClick = {},
            isSaveEnabled = isEditing
        )
    }
}

@Preview(name = "Add Product Sheet (Light)", showBackground = true, widthDp = 428, heightDp = 360)
@Composable
private fun AddProductBottomSheetLightPreview() {
    Theme {
        BottomSheetContentPreviewContent(isEditing = false)
    }
}

@Preview(
    name = "Add Product Sheet (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AddProductBottomSheetDarkPreview() {
    Theme(darkTheme = true) {
        BottomSheetContentPreviewContent(isEditing = false)
    }
}

@Preview(name = "Edit Product Sheet (Light)", showBackground = true, widthDp = 428, heightDp = 360)
@Composable
private fun EditProductBottomSheetLightPreview() {
    Theme {
        BottomSheetContentPreviewContent(isEditing = true)
    }
}

@Preview(
    name = "Edit Product Sheet (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EditProductBottomSheetDarkPreview() {
    Theme(darkTheme = true) {
        BottomSheetContentPreviewContent(isEditing = true)
    }
}

@Composable
private fun ProductDialogPreviewContent(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(name = "Rename Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun RenameListDialogLightPreview() {
    Theme {
        ProductDialogPreviewContent {
            RenameListDialog(currentName = "Выходные", onDismiss = {}, onSave = {})
        }
    }
}

@Preview(
    name = "Rename Dialog (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RenameListDialogDarkPreview() {
    Theme(darkTheme = true) {
        ProductDialogPreviewContent {
            RenameListDialog(currentName = "Выходные", onDismiss = {}, onSave = {})
        }
    }
}

@Preview(name = "Delete List Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun DeleteConfirmDialogLightPreview() {
    Theme {
        ProductDialogPreviewContent {
            DeleteConfirmDialog(onDismiss = {}, onConfirm = {})
        }
    }
}

@Preview(
    name = "Delete List Dialog (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DeleteConfirmDialogDarkPreview() {
    Theme(darkTheme = true) {
        ProductDialogPreviewContent {
            DeleteConfirmDialog(onDismiss = {}, onConfirm = {})
        }
    }
}

@Preview(name = "Clear Bought Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun ClearBoughtConfirmDialogLightPreview() {
    Theme {
        ProductDialogPreviewContent {
            ClearBoughtConfirmDialog(onDismiss = {}, onConfirm = {})
        }
    }
}

@Preview(
    name = "Clear Bought Dialog (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ClearBoughtConfirmDialogDarkPreview() {
    Theme(darkTheme = true) {
        ProductDialogPreviewContent {
            ClearBoughtConfirmDialog(onDismiss = {}, onConfirm = {})
        }
    }
}

@Preview(name = "Cancel Add Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun CancelConfirmDialogAddLightPreview() {
    Theme {
        ProductDialogPreviewContent {
            CancelConfirmDialog(onDismiss = {}, onConfirm = {}, isEditing = false)
        }
    }
}

@Preview(
    name = "Cancel Edit Dialog (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CancelConfirmDialogEditDarkPreview() {
    Theme(darkTheme = true) {
        ProductDialogPreviewContent {
            CancelConfirmDialog(onDismiss = {}, onConfirm = {}, isEditing = true)
        }
    }
}
