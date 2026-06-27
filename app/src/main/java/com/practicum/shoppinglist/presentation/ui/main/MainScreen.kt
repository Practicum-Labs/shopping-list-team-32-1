package com.practicum.shoppinglist.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.ui.main.components.AddShoppingListDialog
import com.practicum.shoppinglist.presentation.ui.main.components.MainActionsRow
import com.practicum.shoppinglist.presentation.ui.main.components.MainEmptyState
import com.practicum.shoppinglist.presentation.ui.main.components.MainErrorState
import com.practicum.shoppinglist.presentation.ui.main.components.MainLoadingState
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListIconPickerBottomSheet
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListsContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoute(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        actions = MainScreenActions(
            onThemeClick = onThemeClick,
            onAddClick = viewModel::onAddListClick,
            onAddListDismiss = viewModel::onAddListDismiss,
            onNewListNameChange = viewModel::onNewListNameChange,
            onCreateListClick = viewModel::createShoppingList,
            onIconPickerDismiss = viewModel::onIconPickerDismiss,
            onIconSelected = viewModel::onShoppingListIconSelected,
            onShoppingListScrollHandled = viewModel::onShoppingListScrollHandled,
            onRetryClick = viewModel::retryShoppingListsLoading,
        ),
        modifier = modifier,
    )
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    actions: MainScreenActions = MainScreenActions(),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Text(
            text = stringResource(id = R.string.main_title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = Dimens.Main.titleStartPadding,
                    top = Dimens.Main.titleTopPadding,
                ),
        )
        MainActionsRow(
            isDarkTheme = isDarkTheme,
            actions = actions,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = Dimens.Main.actionsTopPadding,
                    end = Dimens.Main.actionsEndPadding,
                ),
        )
        MainContent(
            contentState = uiState.contentState,
            isDarkTheme = isDarkTheme,
            scrollToShoppingListId = uiState.scrollToShoppingListId,
            actions = actions,
        )
        FloatingActionButton(
            onClick = actions.onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = Dimens.Main.fabEndPadding,
                    bottom = Dimens.Main.fabBottomPadding,
                )
                .size(Dimens.Main.fabSize),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = Dimens.Main.fabElevation,
                pressedElevation = Dimens.Main.fabElevation,
                focusedElevation = Dimens.Main.fabElevation,
                hoveredElevation = Dimens.Main.fabElevation,
            ),
            shape = RoundedCornerShape(Dimens.Main.fabCornerRadius),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.main_add_list_content_description),
            )
        }
    }

    if (uiState.isAddListDialogVisible) {
        AddShoppingListDialog(
            listName = uiState.newListName,
            isCreatingList = uiState.isCreatingList,
            actions = actions,
        )
    }

    if (uiState.isIconPickerVisible) {
        ShoppingListIconPickerBottomSheet(
            isUpdatingIcon = uiState.isUpdatingIcon,
            isErrorVisible = uiState.isIconPickerErrorVisible,
            actions = actions,
        )
    }
}

@Composable
private fun BoxScope.MainContent(
    contentState: MainContentState,
    isDarkTheme: Boolean,
    scrollToShoppingListId: Long?,
    actions: MainScreenActions,
) {
    when (contentState) {
        MainContentState.Loading -> {
            MainLoadingState(modifier = Modifier.align(Alignment.Center))
        }

        MainContentState.Error -> {
            MainErrorState(
                onRetryClick = actions.onRetryClick,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        MainContentState.Empty -> {
            MainEmptyState(
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = Dimens.Main.emptyStateTopPadding),
            )
        }

        is MainContentState.Content -> {
            ShoppingListsContent(
                shoppingLists = contentState.shoppingLists,
                scrollToShoppingListId = scrollToShoppingListId,
                onShoppingListScrollHandled = actions.onShoppingListScrollHandled,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        start = Dimens.Main.listHorizontalPadding,
                        top = Dimens.Main.listTopPadding,
                        end = Dimens.Main.listHorizontalPadding,
                    ),
            )
        }
    }
}
