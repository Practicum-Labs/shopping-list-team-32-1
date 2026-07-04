package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListMenuBottomSheet(
    sheetState: SheetState,
    currentSortType: SortType,
    onDismissRequest: () -> Unit,
    onSortTypeSelected: (SortType) -> Unit,
    onDeleteAllClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(
            topStart = Dimens.Main.menuSheetCornerRadius,
            topEnd = Dimens.Main.menuSheetCornerRadius,
        ),
        containerColor = MaterialTheme.colors.menuSheetSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        ShoppingListMenuContent(
            currentSortType = currentSortType,
            onSortTypeSelected = onSortTypeSelected,
            onDeleteAllClick = onDeleteAllClick,
            onClearPurchasedClick = onClearPurchasedClick,
        )
    }
}

@Composable
fun ShoppingListMenuContent(
    currentSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit,
    onDeleteAllClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialSortExpanded: Boolean = false,
) {
    var isSortExpanded by remember { mutableStateOf(initialSortExpanded) }
    val sortLabel = when (currentSortType) {
        SortType.Alphabetical -> stringResource(R.string.main_menu_sheet_sort_alphabetical)
        SortType.Custom -> stringResource(R.string.main_menu_sheet_sort_custom)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        MenuDragHandle()

        Spacer(modifier = Modifier.height(Dimens.Main.menuSheetContentTopPadding))

        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
            MenuItemRow(
                icon = painterResource(R.drawable.ic_sort_24),
                iconContentDescription = stringResource(R.string.main_menu_sheet_sort),
                label = stringResource(R.string.main_menu_sheet_sort),
                subtitle = sortLabel,
                backgroundColor = if (isSortExpanded) MaterialTheme.colors.menuSheetSortActive else MaterialTheme.colors.menuSheetSurface,
                onClick = { isSortExpanded = !isSortExpanded },
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Dimens.Main.menuSheetItemIconSize),
                    )
                },
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, 0) {
                            placeable.placeRelative(0, 0)
                        }
                    },
            ) {
                SortSubmenu(
                    expanded = isSortExpanded,
                    currentSortType = currentSortType,
                    onSortTypeSelected = { sortType ->
                        onSortTypeSelected(sortType)
                        isSortExpanded = false
                    },
                )
            }
        }

        MenuItemRow(
            icon = painterResource(R.drawable.ic_deleteall_24),
            iconContentDescription = stringResource(R.string.main_menu_sheet_delete_all),
            label = stringResource(R.string.main_menu_sheet_delete_all),
            onClick = {
                isSortExpanded = false
                onDeleteAllClick()
            },
        )

        MenuItemRow(
            icon = painterResource(R.drawable.ic_deletebuy_24),
            iconContentDescription = stringResource(R.string.main_menu_sheet_clear_purchased),
            label = stringResource(R.string.main_menu_sheet_clear_purchased),
            onClick = {
                isSortExpanded = false
                onClearPurchasedClick()
            },
        )

        Spacer(modifier = Modifier.height(Dimens.Main.menuSheetBottomPadding))
    }
}

@Composable
private fun MenuDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.Main.iconPickerDragHandleTopPadding),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(Dimens.Main.iconPickerDragHandleWidth)
                .height(Dimens.Main.iconPickerDragHandleHeight)
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(Dimens.Main.iconPickerDragHandleHeight),
                ),
        )
    }
}

@Composable
private fun SortSubmenu(
    expanded: Boolean,
    currentSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit,
) {
    if (!expanded) return

    Card(
        modifier = Modifier.width(Dimens.Main.menuSheetSortSubmenuWidth),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.sortSubmenuBackground),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            SortType.entries.forEach { sortType ->
                SortSubmenuItem(
                    sortType = sortType,
                    isSelected = sortType == currentSortType,
                    onSelect = { onSortTypeSelected(sortType) },
                )
            }
        }
    }
}

@Composable
private fun SortSubmenuItem(
    sortType: SortType,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val label = when (sortType) {
        SortType.Alphabetical -> stringResource(R.string.main_menu_sheet_sort_alphabetical)
        SortType.Custom -> stringResource(R.string.main_menu_sheet_sort_custom)
    }
    val icon: Painter = when (sortType) {
        SortType.Alphabetical -> painterResource(R.drawable.ic_sortalpabet_24)
        SortType.Custom -> painterResource(R.drawable.ic_sortcustom_24)
    }
    Surface(
        onClick = onSelect,
        color = MaterialTheme.colors.sortSubmenuBackground,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(Dimens.Main.menuSheetItemIconSize),
            )
            Spacer(modifier = Modifier.width(Dimens.Main.menuSheetItemIconEndPadding))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}

@Composable
private fun MenuItemRow(
    icon: Painter,
    iconContentDescription: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    val resolvedBackground = if (backgroundColor == Color.Unspecified) MaterialTheme.colors.menuSheetSurface else backgroundColor
    Surface(
        onClick = onClick,
        color = resolvedBackground,
        contentColor = MaterialTheme.colors.menuSheetSurface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = Dimens.Main.menuSheetItemHeight)
                .padding(
                    horizontal = Dimens.Main.menuSheetItemHorizontalPadding,
                    vertical = if (subtitle != null) Dimens.Main.menuSheetSortItemVerticalPadding else 0.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = iconContentDescription,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(Dimens.Main.menuSheetItemIconSize),
            )
            Spacer(modifier = Modifier.width(Dimens.Main.menuSheetItemIconEndPadding))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colors.menuSheetSortLabel,
                    )
                }
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}

@Preview(name = "Menu Sheet - Base", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetBasePreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
            )
        }
    }
}


@Preview(name = "Menu Sheet - Base", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetBaseDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
            )
        }
    }
}


@Preview(name = "Menu Sheet - Sort Open (Alphabetical)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortAlphabeticalPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                initialSortExpanded = true,
            )
        }
    }
}


@Preview(name = "Menu Sheet - Sort Open (Alphabetical)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortAlphabeticalDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Alphabetical,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                initialSortExpanded = true,
            )
        }
    }
}


@Preview(name = "Menu Sheet - Sort Open (Custom)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortCustomPreview() {
    Theme {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Custom,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                initialSortExpanded = true,
            )
        }
    }
}


@Preview(name = "Menu Sheet - Sort Open (Custom)", showBackground = true, widthDp = 428)
@Composable
private fun ShoppingListMenuSheetSortCustomDarkPreview() {
    Theme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ShoppingListMenuContent(
                currentSortType = SortType.Custom,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                initialSortExpanded = true,
            )
        }
    }
}

