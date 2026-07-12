package com.practicum.shoppinglist.presentation.ui.products.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.products.ProductItemRow
import com.practicum.shoppinglist.presentation.ui.products.ProductSwipeBackground

@Composable
private fun ProductItemStatesPreviewContent(darkTheme: Boolean) {
    Theme(darkTheme = darkTheme) {
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
}

@Preview(name = "Product Item States (Light)", showBackground = true, widthDp = 428, heightDp = 500)
@Composable
private fun ProductItemStatesLightPreview() {
    ProductItemStatesPreviewContent(darkTheme = false)
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
    ProductItemStatesPreviewContent(darkTheme = true)
}
