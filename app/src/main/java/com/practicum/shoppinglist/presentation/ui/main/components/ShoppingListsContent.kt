package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.shoppingListIconByName
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ShoppingListsContent(
    shoppingLists: List<ShoppingList>,
    scrollToShoppingListId: Long?,
    onShoppingListIconClick: (Long) -> Unit,
    onShoppingListScrollHandled: () -> Unit,
    onDeleteListClick: (Long) -> Unit,
    onCopyListClick: (Long, String) -> Unit,
    onRenameListClick: (Long) -> Unit,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isSearching: Boolean = false,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(scrollToShoppingListId, shoppingLists) {
        scrollToShoppingListId?.let { targetListId ->
            val targetIndex = shoppingLists.indexOfFirst { shoppingList ->
                shoppingList.id == targetListId
            }

            if (targetIndex >= 0) {
                listState.animateScrollToItem(index = targetIndex)
                onShoppingListScrollHandled()
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            top = Dimens.Main.listContentTopPadding,
            bottom = Dimens.Main.listContentBottomPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Main.listItemSpacing),
    ) {
        items(
            items = shoppingLists,
            key = { shoppingList -> shoppingList.id },
        ) { shoppingList ->
            if (isSearching) {
                SearchShoppingListItem(
                    shoppingList = shoppingList,
                    onIconClick = onShoppingListIconClick,
                    onListClick = onListClick,
                )
            } else {
                SwipeableListItem(
                    onDelete = { onDeleteListClick(shoppingList.id) },
                    backgroundContent = { isLongSwipe, closeItem ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(start = Dimens.Main.swipeActionContainerStartPadding, end = 0.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (isLongSwipe) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        modifier = Modifier.size(Dimens.Main.swipeActionButtonSize),
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = stringResource(
                                                    id = R.string.main_delete_content_description
                                                ),
                                                modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                                            )
                                        }
                                    }
                                }
                            } else {
                                val copiedName = stringResource(id = R.string.main_copy_suffix, shoppingList.name)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = {
                                            closeItem()
                                            onRenameListClick(shoppingList.id)
                                        },
                                        modifier = Modifier
                                            .size(Dimens.Main.swipeActionButtonSize)
                                            .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = stringResource(id = R.string.main_rename_dialog_title),
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(Dimens.Main.swipeActionSpacing))
                                    IconButton(
                                        onClick = {
                                            closeItem()
                                            onCopyListClick(shoppingList.id, copiedName)
                                        },
                                        modifier = Modifier
                                            .size(Dimens.Main.swipeActionButtonSize)
                                            .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ContentCopy,
                                            contentDescription = stringResource(
                                                id = R.string.main_copy_content_description
                                            ),
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(Dimens.Main.swipeActionSpacing))
                                    IconButton(
                                        onClick = {
                                            closeItem()
                                            onDeleteListClick(shoppingList.id)
                                        },
                                        modifier = Modifier
                                            .size(Dimens.Main.swipeActionButtonSize)
                                            .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = stringResource(
                                                id = R.string.main_delete_content_description
                                            ),
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                                        )
                                    }
                                }
                            }
                        }
                    },
                    content = {
                        ShoppingListItem(
                            shoppingList = shoppingList,
                            onIconClick = onShoppingListIconClick,
                            onListClick = onListClick,
                        )
                    }
                )
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
fun SwipeableListItem(
    onDelete: () -> Unit,
    backgroundContent: @Composable (isLongSwipe: Boolean, closeItem: () -> Unit) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val actionsWidth = Dimens.Main.swipeActionsWidth
    val actionsWidthPx = with(density) { -actionsWidth.toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()

    val isLongSwipe = offsetX < actionsWidthPx * 1.5f

    val closeItem: () -> Unit = {
        coroutineScope.launch {
            androidx.compose.animation.core.animate(
                initialValue = offsetX,
                targetValue = 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) { value, _ ->
                offsetX = value
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(Dimens.Main.listItemCornerRadius))
        ) {
            backgroundContent(isLongSwipe, closeItem)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val targetOffset = if (offsetX < actionsWidthPx * 1.5f) {
                                onDelete()
                                0f
                            } else if (offsetX < actionsWidthPx / 2f) {
                                actionsWidthPx
                            } else {
                                0f
                            }
                            coroutineScope.launch {
                                androidx.compose.animation.core.animate(
                                    initialValue = offsetX,
                                    targetValue = targetOffset,
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                ) { value, _ ->
                                    offsetX = value
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceAtMost(0f)
                        }
                    )
                }
        ) {
            content()
        }
    }
}

@Composable
private fun ShoppingListItem(
    shoppingList: ShoppingList,
    onIconClick: (Long) -> Unit,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Dimens.Main.listItemCornerRadius)

    Card(
        onClick = { onListClick(shoppingList.id) },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.Main.listItemMinHeight)
            .shadow(
                elevation = Dimens.Main.listItemElevation,
                shape = shape,
                clip = false,
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.listItemSurface,
            contentColor = MaterialTheme.colors.listItemTitle,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.Main.listItemHorizontalPadding,
                    vertical = Dimens.Main.listItemVerticalPadding,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Main.listItemContentSpacing),
        ) {
            Surface(
                onClick = { onIconClick(shoppingList.id) },
                modifier = Modifier
                    .size(Dimens.Main.listItemIconContainerSize),
                shape = CircleShape,
                color = MaterialTheme.colors.iconPickerItemContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = shoppingListIconByName(shoppingList.iconName),
                        contentDescription = stringResource(
                            id = R.string.main_shopping_list_icon_content_description,
                        ),
                        modifier = Modifier.size(Dimens.Main.listItemIconSize),
                    )
                }
            }
            Text(
                text = shoppingList.name,
                color = MaterialTheme.colors.listItemTitle,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SearchShoppingListItem(
    shoppingList: ShoppingList,
    onIconClick: (Long) -> Unit,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable { onListClick(shoppingList.id) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Main.listItemContentSpacing),
    ) {
        Surface(
            onClick = { onIconClick(shoppingList.id) },
            modifier = Modifier
                .size(Dimens.Main.listItemIconContainerSize),
            shape = CircleShape,
            color = MaterialTheme.colors.iconPickerItemContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = shoppingListIconByName(shoppingList.iconName),
                    contentDescription = stringResource(
                        id = R.string.main_shopping_list_icon_content_description,
                    ),
                    modifier = Modifier.size(Dimens.Main.listItemIconSize),
                )
            }
        }
        Text(
            text = shoppingList.name,
            color = MaterialTheme.colors.listItemTitle,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
