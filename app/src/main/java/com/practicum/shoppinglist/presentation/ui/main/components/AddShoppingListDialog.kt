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
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
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
fun AddShoppingListDialog(
    listName: String,
    isCreatingList: Boolean,
    actions: MainScreenActions,
) {
    val focusRequester = remember { FocusRequester() }

    Dialog(
        onDismissRequest = {
            if (!isCreatingList) {
                actions.onAddListDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isCreatingList,
            dismissOnClickOutside = !isCreatingList,
        ),
    ) {
        AddShoppingListDialogContent(
            listName = listName,
            isCreatingList = isCreatingList,
            onListNameChange = actions.onNewListNameChange,
            onDismiss = actions.onAddListDismiss,
            onCreateClick = actions.onCreateListClick,
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
fun AddShoppingListDialogContent(
    listName: String,
    isCreatingList: Boolean,
    onListNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onCreateClick: () -> Unit,
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
                imageVector = Icons.AutoMirrored.Outlined.PlaylistAdd,
                contentDescription = stringResource(
                    id = R.string.main_add_list_dialog_icon_content_description,
                ),
                tint = colors.addListDialogIcon,
                modifier = Modifier.size(Dimens.Main.addDialogIconSize),
            )
            Spacer(modifier = Modifier.height(Dimens.Main.addDialogTitleTopPadding))
            Text(
                text = stringResource(id = R.string.main_add_list_dialog_title),
                color = colors.addListDialogTitle,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(Dimens.Main.addDialogFieldTopPadding))
            AddShoppingListNameField(
                value = listName,
                onValueChange = onListNameChange,
                enabled = !isCreatingList,
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
                    enabled = !isCreatingList,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.addListDialogAccent,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.main_add_list_cancel))
                }
                Spacer(modifier = Modifier.width(Dimens.Main.addDialogActionSpacing))
                TextButton(
                    onClick = onCreateClick,
                    enabled = listName.isNotBlank() && !isCreatingList,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.addListDialogAccent,
                        disabledContentColor = colors.addListDialogAccent,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.main_add_list_create))
                }
            }
        }
    }
}

@Composable
private fun AddShoppingListNameField(
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
                            text = stringResource(id = R.string.main_add_list_name_placeholder),
                            color = placeholderColor,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    innerTextField()
                }
            },
        )
        Text(
            text = stringResource(id = R.string.main_add_list_name_label),
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
