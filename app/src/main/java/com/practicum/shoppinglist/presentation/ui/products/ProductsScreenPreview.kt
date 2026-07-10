package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.shoppinglist.domain.model.ShoppingItem

@Suppress("MagicNumber")
@Preview(showBackground = true, name = "Light Theme")
@Preview(showBackground = true, name = "Dark Theme", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductsScreenPreview() {
    com.practicum.shoppinglist.presentation.theme.Theme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                val mockState = ProductsUiState(
                    list = com.practicum.shoppinglist.domain.model.ShoppingList(1L, "Продукты", "list_alt"),
                    items = listOf(
                        ShoppingItem(1L, 1L, "Молоко", 1.5, "кг", false),
                        ShoppingItem(2L, 1L, "Хлеб", 1.0, "шт", true)
                    ),
                    isLoading = false
                )
                Column {
                    ProductsTopBar(
                        title = mockState.list?.name ?: "Продукты",
                        onBack = {},
                        onMenuClick = {}
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        ProductList(
                            items = mockState.items,
                            onToggleBought = {},
                            onDelete = {},
                            onEdit = {},
                            onMove = { _, _ -> }
                        )
                    }
                }
            }
        }
    }
}
