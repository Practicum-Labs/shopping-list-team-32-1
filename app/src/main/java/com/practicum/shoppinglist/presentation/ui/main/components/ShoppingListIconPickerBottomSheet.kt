package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.MainScreenActions
import com.practicum.shoppinglist.presentation.ui.main.ShoppingListIconOptions
import androidx.compose.foundation.lazy.grid.items as gridItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListIconPickerBottomSheet(
    isUpdatingIcon: Boolean,
    isErrorVisible: Boolean,
    actions: MainScreenActions,
) {
    val sheetHeight = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.height.toDp() *
            Dimens.Main.iconPickerSheetHeightFraction
    }

    ModalBottomSheet(
        onDismissRequest = actions.onIconPickerDismiss,
        containerColor = MaterialTheme.colors.iconPickerSheetSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        dragHandle = null,
    ) {
        ShoppingListIconPickerContent(
            isUpdatingIcon = isUpdatingIcon,
            isErrorVisible = isErrorVisible,
            onIconSelected = actions.onIconSelected,
            modifier = Modifier.height(sheetHeight),
        )
    }
}

@Composable
fun ShoppingListIconPickerContent(
    isUpdatingIcon: Boolean,
    isErrorVisible: Boolean,
    onIconSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = Dimens.Main.iconPickerDragHandleTopPadding)
                .width(Dimens.Main.iconPickerDragHandleWidth)
                .height(Dimens.Main.iconPickerDragHandleHeight)
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(Dimens.Main.iconPickerDragHandleHeight),
                ),
        )
        if (isErrorVisible) {
            Text(
                text = stringResource(id = R.string.main_icon_picker_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        start = Dimens.Main.iconPickerErrorHorizontalPadding,
                        top = Dimens.Main.iconPickerErrorTopPadding,
                        end = Dimens.Main.iconPickerErrorHorizontalPadding,
                    ),
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 5),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = Dimens.Main.iconPickerHorizontalPadding,
                    top = if (isErrorVisible) {
                        Dimens.Main.iconPickerTopPaddingWithError
                    } else {
                        Dimens.Main.iconPickerTopPadding
                    },
                    end = Dimens.Main.iconPickerHorizontalPadding,
                    bottom = Dimens.Main.iconPickerBottomPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Main.iconPickerGridSpacing),
            verticalArrangement = Arrangement.spacedBy(Dimens.Main.iconPickerGridSpacing),
        ) {
            gridItems(
                items = ShoppingListIconOptions,
                key = { iconOption -> iconOption.name },
            ) { iconOption ->
                IconButton(
                    onClick = { onIconSelected(iconOption.name) },
                    enabled = !isUpdatingIcon,
                    modifier = Modifier.size(Dimens.Main.iconPickerItemSize),
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.Main.iconPickerItemSize)
                            .background(
                                color = MaterialTheme.colors.iconPickerItemContainer,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = iconOption.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(Dimens.Main.iconPickerIconSize),
                        )
                    }
                }
            }
        }
    }
}
