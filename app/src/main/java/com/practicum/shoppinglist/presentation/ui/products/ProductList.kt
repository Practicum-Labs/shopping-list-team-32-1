package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
fun ProductsContent(
    innerPadding: PaddingValues,
    state: ProductsUiState,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit,
    onMove: (Int, Int) -> Unit
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
                onMove = onMove
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
    onMove: (Int, Int) -> Unit
) {
    val state = rememberLazyListState()
    val dragDropState = remember { DragDropState(state, onMove) }
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Dimens.Products.listVerticalPadding)
            .dragDropGesture(dragDropState)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            val offset by animateDpAsState(
                targetValue = dragDropState.getItemOffset(index).y.dp,
                label = "offset"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { translationY = offset.toPx() }
            ) {
                SwipeableProductItem(
                    item = item,
                    onToggleBought = onToggleBought,
                    onDelete = onDelete,
                    onEdit = onEdit
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableProductItem(
    item: ShoppingItem,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit
) {
    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        when (state.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                onDelete(item)
            }
            SwipeToDismissBoxValue.StartToEnd -> {
                onEdit(item)
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
            SwipeToDismissBoxValue.Settled -> {}
        }
    }
    SwipeToDismissBox(
        state = state,
        backgroundContent = { SwipeBackground(state.targetValue) },
        content = { ProductItemRow(item = item, onToggleBought = onToggleBought) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(target: SwipeToDismissBoxValue) {
    val color = when (target) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colors.swipeActionBackground
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Transparent
    }
    val alignment = if (target == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
    val icon = if (target == SwipeToDismissBoxValue.StartToEnd) Icons.Default.Edit else Icons.Default.Delete
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = Dimens.Products.sheetHorizontalPadding),
        contentAlignment = alignment
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
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
