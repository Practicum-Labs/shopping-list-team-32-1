package com.practicum.shoppinglist.presentation.ui.products.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.products.CancelConfirmDialog
import com.practicum.shoppinglist.presentation.ui.products.DeleteConfirmDialog
import com.practicum.shoppinglist.presentation.ui.products.RenameListDialog

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

@Composable
private fun RenameListDialogPreviewContent(darkTheme: Boolean) {
    Theme(darkTheme = darkTheme) {
        ProductDialogPreviewContent {
            RenameListDialog(currentName = "Выходные", onDismiss = {}, onSave = {})
        }
    }
}

@Preview(name = "Rename Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun RenameListDialogLightPreview() {
    RenameListDialogPreviewContent(darkTheme = false)
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
    RenameListDialogPreviewContent(darkTheme = true)
}

@Composable
private fun DeleteConfirmDialogPreviewContent(darkTheme: Boolean) {
    Theme(darkTheme = darkTheme) {
        ProductDialogPreviewContent {
            DeleteConfirmDialog(onDismiss = {}, onConfirm = {})
        }
    }
}

@Preview(name = "Delete List Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun DeleteConfirmDialogLightPreview() {
    DeleteConfirmDialogPreviewContent(darkTheme = false)
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
    DeleteConfirmDialogPreviewContent(darkTheme = true)
}

@Composable
private fun CancelConfirmDialogPreviewContent(darkTheme: Boolean, isEditing: Boolean) {
    Theme(darkTheme = darkTheme) {
        ProductDialogPreviewContent {
            CancelConfirmDialog(onDismiss = {}, onConfirm = {}, isEditing = isEditing)
        }
    }
}

@Preview(name = "Cancel Add Dialog (Light)", showBackground = true, widthDp = 428, heightDp = 400)
@Composable
private fun CancelConfirmDialogAddLightPreview() {
    CancelConfirmDialogPreviewContent(darkTheme = false, isEditing = false)
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
    CancelConfirmDialogPreviewContent(darkTheme = true, isEditing = false)
}
