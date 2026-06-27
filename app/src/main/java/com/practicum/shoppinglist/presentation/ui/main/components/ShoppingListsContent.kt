package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.shoppingListIconByName

@Composable
fun ShoppingListsContent(
    shoppingLists: List<ShoppingList>,
    scrollToShoppingListId: Long?,
    onShoppingListScrollHandled: () -> Unit,
    modifier: Modifier = Modifier,
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
            ShoppingListItem(shoppingList = shoppingList)
        }
    }
}

@Composable
private fun ShoppingListItem(
    shoppingList: ShoppingList,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Dimens.Main.listItemCornerRadius)

    Card(
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
            Box(
                modifier = Modifier
                    .size(Dimens.Main.listItemIconContainerSize)
                    .background(
                        color = MaterialTheme.colors.iconPickerItemContainer,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = shoppingListIconByName(shoppingList.iconName),
                    contentDescription = stringResource(
                        id = R.string.main_shopping_list_icon_content_description,
                    ),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(Dimens.Main.listItemIconSize),
                )
            }
            Text(
                text = shoppingList.name,
                color = MaterialTheme.colors.listItemTitle,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
