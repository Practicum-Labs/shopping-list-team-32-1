package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.components.SwipeableListItem

@Composable
fun ProductsContent(
    innerPadding: PaddingValues,
    state: ProductsUiState,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (state.items.isEmpty()) {
            EmptyState()
        } else {
            ProductList(
                items = state.items,
                onToggleBought = onToggleBought,
                onDelete = onDelete,
                onEdit = onEdit,
                onMove = onMove,
                onDragEnd = onDragEnd
            )
        }
    }
}

@Composable
fun ProductList(
    items: List<ShoppingItem>,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit = {}
) {
    val state = rememberLazyListState()
    val dragDropState = remember { DragDropState(state, onMove, onDragEnd) }
    val draggedIndex = dragDropState.draggedIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Dimens.Products.listVerticalPadding)
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .dragDropGesture(dragDropState)
        ) {
            itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(if (index == draggedIndex) 0f else 1f)
                ) {
                    SwipeableProductItem(
                        item = item,
                        onToggleBought = onToggleBought,
                        onDelete = onDelete,
                        onEdit = onEdit,
                        isDragging = draggedIndex != null
                    )
                }
            }
        }

        if (draggedIndex != null && draggedIndex in items.indices) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .offset { dragDropState.overlayOffset() }
            ) {
                SwipeableProductItem(
                    item = items[draggedIndex],
                    onToggleBought = onToggleBought,
                    onDelete = onDelete,
                    onEdit = onEdit
                )
            }
        }
    }
}

@Composable
fun SwipeableProductItem(
    item: ShoppingItem,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit,
    isDragging: Boolean = false
) {
    SwipeableListItem(
        onDelete = { onDelete(item) },
        actionsWidth = Dimens.Main.swipeActionsWidthTwoButtons,
        backgroundContent = { isLongSwipe, closeItem ->
            ProductSwipeBackground(
                isLongSwipe = isLongSwipe,
                onEditClick = { closeItem(); onEdit(item) },
                onDeleteClick = { closeItem(); onDelete(item) }
            )
        },
        content = { ProductItemRow(item = item, onToggleBought = onToggleBought, isDragging = isDragging) }
    )
}

@Suppress("LongMethod")
@Composable
fun ProductSwipeBackground(
    isLongSwipe: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = Dimens.Main.swipeActionContainerStartPadding, end = 0.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (isLongSwipe) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(Dimens.Main.swipeActionButtonSize),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(id = R.string.products_delete_content_description),
                            modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                        )
                    }
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(Dimens.Main.swipeActionButtonSize)
                        .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(id = R.string.products_edit_content_description),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                    )
                }
                Spacer(modifier = Modifier.width(Dimens.Main.swipeActionSpacing))
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(Dimens.Main.swipeActionButtonSize)
                        .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.products_delete_content_description),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCheckbox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkedBgColor = MaterialTheme.colors.productCheckedBackground
    val checkmarkColor = MaterialTheme.colors.productTick

    Box(
        modifier = modifier
            .size(Dimens.Products.checkboxSize)
            .background(
                color = if (checked) checkedBgColor else Color.Transparent,
                shape = CircleShape
            )
            .border(
                width = Dimens.Products.checkboxBorderWidth,
                color = if (checked) checkedBgColor else MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onCheckedChange),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(Dimens.Products.checkboxIconSize)
            )
        }
    }
}
