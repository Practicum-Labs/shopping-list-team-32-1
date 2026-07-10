package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
fun RenameDialogWrapper(
    visible: Boolean,
    currentName: String,
    onRename: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {
        RenameListDialog(
            currentName = currentName,
            onDismiss = onDismiss,
            onSave = {
                onRename(it)
                onDismiss()
            }
        )
    }
}

@Composable
fun DeleteConfirmDialogWrapper(
    visible: Boolean,
    onDeleteConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {
        DeleteConfirmDialog(
            onDismiss = onDismiss,
            onConfirm = {
                onDeleteConfirm()
                onDismiss()
            }
        )
    }
}

@Composable
fun RenameListDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.products_dialog_rename_title),
                color = colors.addListDialogTitle
            )
        },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.products_dialog_rename_label)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.addListDialogAccent,
                    focusedLabelColor = colors.addListDialogTitle,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name) },
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = stringResource(R.string.products_dialog_rename_save),
                    color = colors.addListDialogAccent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.products_dialog_cancel),
                    color = colors.addListDialogPlaceholder
                )
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun DeleteConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.products_dialog_delete_title),
                color = colors.addListDialogTitle
            )
        },
        text = {
            Text(
                text = stringResource(R.string.products_dialog_delete_message),
                color = colors.addListDialogPlaceholder
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.products_dialog_delete_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.products_dialog_cancel),
                    color = colors.addListDialogPlaceholder
                )
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun ClearBoughtConfirmDialogWrapper(
    visible: Boolean,
    onClearConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {
        ClearBoughtConfirmDialog(
            onDismiss = onDismiss,
            onConfirm = {
                onClearConfirm()
                onDismiss()
            }
        )
    }
}

@Composable
fun ClearBoughtConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.products_dialog_clear_bought_title),
                color = colors.addListDialogTitle
            )
        },
        text = {
            Text(
                text = stringResource(R.string.products_dialog_clear_bought_message),
                color = colors.addListDialogPlaceholder
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.products_dialog_clear_bought_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.products_dialog_cancel),
                    color = colors.addListDialogPlaceholder
                )
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun CancelConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isEditing: Boolean = false
) {
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    if (isEditing) {
                        R.string.products_dialog_cancel_edit_confirm_title
                    } else {
                        R.string.products_dialog_cancel_confirm_title
                    }
                ),
                color = colors.addListDialogTitle
            )
        },
        text = {
            Text(
                text = stringResource(R.string.products_dialog_cancel_confirm_message),
                color = colors.addListDialogPlaceholder
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.products_dialog_cancel_confirm_yes),
                    color = colors.addListDialogAccent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.products_dialog_cancel_confirm_no),
                    color = colors.addListDialogPlaceholder
                )
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun CancelConfirmDialogWrapper(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isEditing: Boolean = false
) {
    if (visible) {
        CancelConfirmDialog(
            onDismiss = onDismiss,
            onConfirm = {
                onConfirm()
                onDismiss()
            },
            isEditing = isEditing
        )
    }
}
