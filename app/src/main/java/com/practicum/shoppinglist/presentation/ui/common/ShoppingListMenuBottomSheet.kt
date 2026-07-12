package com.practicum.shoppinglist.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListMenuBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onDeleteAllClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
    currentSortType: SortType = SortType.Alphabetical,
    onSortTypeSelected: (SortType) -> Unit = {},
    sortLabel: String? = null,
    onSortClick: (() -> Unit)? = null,
    onRenameClick: (() -> Unit)? = null,
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
            sortLabel = sortLabel,
            onSortClick = onSortClick,
            onRenameClick = onRenameClick,
        )
    }
}

@Composable
internal fun ShoppingListMenuContent(
    currentSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit,
    onDeleteAllClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
    modifier: Modifier = Modifier,
    sortLabel: String? = null,
    onSortClick: (() -> Unit)? = null,
    onRenameClick: (() -> Unit)? = null,
    initialSortExpanded: Boolean = false,
) {
    var isSortExpanded by remember { mutableStateOf(initialSortExpanded) }
    val isSortSingleAction = onSortClick != null
    val resolvedSortLabel = sortLabel ?: stringResource(R.string.main_menu_sheet_sort)
    val sortSubtitle = if (isSortSingleAction) {
        null
    } else {
        when (currentSortType) {
            SortType.Alphabetical -> stringResource(R.string.main_menu_sheet_sort_alphabetical)
            SortType.Custom -> stringResource(R.string.main_menu_sheet_sort_custom)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BottomSheetDragHandle()

        Spacer(modifier = Modifier.height(Dimens.Main.menuSheetContentTopPadding))

        SortMenuRow(
            label = resolvedSortLabel,
            subtitle = sortSubtitle,
            isExpanded = isSortExpanded,
            isSingleAction = isSortSingleAction,
            currentSortType = currentSortType,
            onRowClick = {
                if (isSortSingleAction) {
                    onSortClick()
                } else {
                    isSortExpanded = !isSortExpanded
                }
            },
            onSortTypeSelected = { sortType ->
                onSortTypeSelected(sortType)
                isSortExpanded = false
            },
        )

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

        if (onRenameClick != null) {
            MenuItemRow(
                icon = rememberVectorPainter(Icons.Default.Edit),
                iconContentDescription = stringResource(R.string.products_menu_rename),
                label = stringResource(R.string.products_menu_rename),
                onClick = {
                    isSortExpanded = false
                    onRenameClick()
                },
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Main.menuSheetBottomPadding))
    }
}
