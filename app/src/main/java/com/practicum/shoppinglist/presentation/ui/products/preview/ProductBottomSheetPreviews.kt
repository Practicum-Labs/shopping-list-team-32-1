package com.practicum.shoppinglist.presentation.ui.products.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.products.BottomSheetContent

@Composable
private fun BottomSheetContentPreviewContent(darkTheme: Boolean, isEditing: Boolean) {
    Theme(darkTheme = darkTheme) {
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
}

@Preview(name = "Edit Product Sheet (Light)", showBackground = true, widthDp = 428, heightDp = 360)
@Composable
private fun EditProductBottomSheetLightPreview() {
    BottomSheetContentPreviewContent(darkTheme = false, isEditing = true)
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
    BottomSheetContentPreviewContent(darkTheme = true, isEditing = true)
}
