package com.practicum.shoppinglist.presentation.ui.common

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
internal fun SortMenuRow(
    label: String,
    subtitle: String?,
    isExpanded: Boolean,
    isSingleAction: Boolean,
    currentSortType: SortType,
    onRowClick: () -> Unit,
    onSortTypeSelected: (SortType) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
        MenuItemRow(
            icon = painterResource(R.drawable.ic_sort_24),
            iconContentDescription = label,
            label = label,
            subtitle = subtitle,
            backgroundColor = if (isExpanded) {
                MaterialTheme.colors.menuSheetSortActive
            } else {
                MaterialTheme.colors.menuSheetSurface
            },
            onClick = onRowClick,
            trailing = if (isSingleAction) {
                null
            } else {
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Dimens.Main.menuSheetItemIconSize),
                    )
                }
            },
        )
        if (!isSingleAction) {
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
                    expanded = isExpanded,
                    currentSortType = currentSortType,
                    onSortTypeSelected = onSortTypeSelected,
                )
            }
        }
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
        shape = RoundedCornerShape(Dimens.Main.sortSubmenuCornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Main.sortSubmenuElevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.sortSubmenuBackground),
    ) {
        Column(modifier = Modifier.padding(vertical = Dimens.Main.sortSubmenuVerticalPadding)) {
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
                .height(Dimens.Main.sortSubmenuItemHeight)
                .padding(horizontal = Dimens.Main.sortSubmenuItemHorizontalPadding),
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
