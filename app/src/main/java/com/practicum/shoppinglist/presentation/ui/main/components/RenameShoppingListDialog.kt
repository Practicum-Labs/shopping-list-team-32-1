package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.MainScreenActions

@Composable
fun RenameShoppingListDialog(
    listName: String,
    isRenamingList: Boolean,
    actions: MainScreenActions,
) {
    val focusRequester = remember { FocusRequester() }

    Dialog(
        onDismissRequest = {
            if (!isRenamingList) {
                actions.onRenameListDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isRenamingList,
            dismissOnClickOutside = !isRenamingList,
        ),
    ) {
        RenameShoppingListDialogContent(
            listName = listName,
            isRenamingList = isRenamingList,
            onListNameChange = actions.onRenameListNameChange,
            onDismiss = actions.onRenameListDismiss,
            onSaveClick = actions.onRenameListConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Main.addDialogHorizontalPadding),
            textFieldModifier = Modifier.focusRequester(focusRequester),
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun RenameShoppingListDialogContent(
    listName: String,
    isRenamingList: Boolean,
    onListNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colors

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Main.addDialogCornerRadius),
        color = colors.addListDialogSurface,
        contentColor = colors.addListDialogTitle,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimens.Main.addDialogHorizontalContentPadding,
                    top = Dimens.Main.addDialogTopPadding,
                    end = Dimens.Main.addDialogHorizontalContentPadding,
                    bottom = Dimens.Main.addDialogBottomPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(
                    id = R.string.main_rename_dialog_title,
                ),
                tint = colors.addListDialogIcon,
                modifier = Modifier.size(Dimens.Main.addDialogIconSize),
            )
            Spacer(modifier = Modifier.height(Dimens.Main.addDialogTitleTopPadding))
            Text(
                text = stringResource(id = R.string.main_rename_dialog_title),
                color = colors.addListDialogTitle,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(Dimens.Main.addDialogFieldTopPadding))
            RenameShoppingListNameField(
                value = listName,
                onValueChange = onListNameChange,
                enabled = !isRenamingList,
                modifier = textFieldModifier.fillMaxWidth(),
                labelContainerColor = colors.addListDialogLabelContainer,
                accentColor = colors.addListDialogAccent,
                placeholderColor = colors.addListDialogPlaceholder,
            )
            Spacer(modifier = Modifier.height(Dimens.Main.addDialogActionsTopPadding))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = onDismiss,
                    enabled = !isRenamingList,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.addListDialogAccent,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.main_rename_dialog_cancel))
                }
                Spacer(modifier = Modifier.width(Dimens.Main.addDialogActionSpacing))
                TextButton(
                    onClick = onSaveClick,
                    enabled = listName.isNotBlank() && !isRenamingList,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.addListDialogAccent,
                        disabledContentColor = colors.addListDialogAccent,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.main_rename_dialog_save))
                }
            }
        }
    }
}

@Composable
private fun RenameShoppingListNameField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    labelContainerColor: Color,
    accentColor: Color,
    placeholderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.Main.addDialogLabelHorizontalPadding)
                .height(Dimens.Main.addDialogFieldHeight)
                .border(
                    width = Dimens.Main.addDialogFieldBorderWidth,
                    color = accentColor,
                    shape = RoundedCornerShape(Dimens.Main.addDialogFieldCornerRadius),
                )
                .padding(
                    horizontal = Dimens.Main.addDialogFieldHorizontalPadding,
                    vertical = Dimens.Main.addDialogFieldVerticalPadding,
                ),
            enabled = enabled,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = placeholderColor),
            cursorBrush = SolidColor(accentColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.main_rename_dialog_name_label),
                            color = placeholderColor,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    innerTextField()
                }
            },
        )
        Text(
            text = stringResource(id = R.string.main_rename_dialog_name_label),
            color = accentColor,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = Dimens.Main.addDialogLabelStartPadding)
                .background(labelContainerColor)
                .padding(horizontal = Dimens.Main.addDialogLabelHorizontalPadding),
        )
    }
}
