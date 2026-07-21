package com.practicum.shoppinglist.presentation.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.common.ConfirmationDialog
import com.practicum.shoppinglist.presentation.ui.main.components.AddShoppingListDialog
import com.practicum.shoppinglist.presentation.ui.main.components.MainActionsRow
import com.practicum.shoppinglist.presentation.ui.main.components.MainEmptyState
import com.practicum.shoppinglist.presentation.ui.main.components.MainErrorState
import com.practicum.shoppinglist.presentation.ui.main.components.MainLoadingState
import com.practicum.shoppinglist.presentation.ui.main.components.RenameShoppingListDialog
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListIconPickerBottomSheet
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListsContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoute(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    selectedListId: Long? = null,
    viewModel: MainViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.checkSessionInBackground(onInvalidSession = onLogoutClick)
    }

    MainScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        actions = MainScreenActions(
            onThemeClick = onThemeClick,
            onLogoutClick = { viewModel.onLogoutClick(onSuccess = onLogoutClick) },
            onAddClick = viewModel::onAddListClick,
            onAddListDismiss = viewModel::onAddListDismiss,
            onNewListNameChange = viewModel::onNewListNameChange,
            onCreateListClick = viewModel::createShoppingList,
            onIconPickerDismiss = viewModel::onIconPickerDismiss,
            onShoppingListIconClick = viewModel::onShoppingListIconClick,
            onIconSelected = viewModel::onShoppingListIconSelected,
            onShoppingListScrollHandled = viewModel::onShoppingListScrollHandled,
            onRetryClick = viewModel::retryShoppingListsLoading,
            onSearchClick = viewModel::onSearchClick,
            onSearchBack = viewModel::onSearchBack,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onDeleteClick = viewModel::onDeleteAllClick,
            onDeleteAllConfirm = viewModel::onDeleteAllConfirm,
            onDeleteAllDismiss = viewModel::onDeleteAllDismiss,
            onDeleteListClick = viewModel::onDeleteListClick,
            onDeleteListConfirm = viewModel::onDeleteListConfirm,
            onDeleteListDismiss = viewModel::onDeleteListDismiss,
            onCopyListClick = viewModel::onCopyListClick,
            onRenameListClick = viewModel::onRenameListClick,
            onRenameListNameChange = viewModel::onRenameListNameChange,
            onRenameListConfirm = viewModel::onRenameListConfirm,
            onRenameListDismiss = viewModel::onRenameListDismiss,
            onListClick = onListClick,
        ),
        selectedListId = selectedListId,
        modifier = modifier,
    )
}

@Composable
@Suppress("CognitiveComplexMethod", "CyclomaticComplexMethod")
fun MainScreen(
    uiState: MainUiState,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    actions: MainScreenActions = MainScreenActions(),
    selectedListId: Long? = null,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.isSearching) {
        if (uiState.isSearching) {
            focusRequester.requestFocus()
        }
    }

    BackHandler(enabled = uiState.isSearching) {
        actions.onSearchBack()
    }

    val backgroundColor = if (uiState.isSearching && uiState.searchQuery.isNotEmpty()) {
        MaterialTheme.colors.addListDialogSurface
    } else {
        MaterialTheme.colorScheme.background
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        if (uiState.isSearching) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Main.actionButtonSize + Dimens.Main.actionsTopPadding * 2)
                        .background(MaterialTheme.colors.addListDialogSurface)
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = actions.onSearchBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.main_search_back),
                            tint = MaterialTheme.colors.addListDialogIcon
                        )
                    }
                    BasicTextField(
                        value = uiState.searchQuery,
                        onValueChange = actions.onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .padding(horizontal = 8.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (uiState.searchQuery.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.main_search_placeholder),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { actions.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.main_search_clear),
                                tint = MaterialTheme.colors.addListDialogIcon
                            )
                        }
                    }
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
            }
        } else {
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
        }
        MainContent(
            contentState = uiState.contentState,
            isDarkTheme = isDarkTheme,
            isSearching = uiState.isSearching,
            scrollToShoppingListId = uiState.scrollToShoppingListId,
            selectedListId = selectedListId,
            actions = actions,
        )
        if (uiState.isSearching && uiState.searchQuery.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dimens.Main.actionButtonSize + Dimens.Main.actionsTopPadding * 2)
                    .background(Color.Black.copy(alpha = 0.36f))
            )
        }
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

    if (uiState.isDeleteAllDialogVisible) {
        ConfirmationDialog(
            title = stringResource(id = R.string.main_delete_all_confirm_title),
            confirmText = stringResource(id = R.string.main_delete_confirm_yes),
            cancelText = stringResource(id = R.string.main_delete_confirm_no),
            onConfirm = actions.onDeleteAllConfirm,
            onDismiss = actions.onDeleteAllDismiss,
        )
    }

    uiState.deleteListConfirmDialogTarget?.let { targetList ->
        ConfirmationDialog(
            title = stringResource(id = R.string.main_delete_list_confirm_title, targetList.name),
            confirmText = stringResource(id = R.string.main_delete_confirm_yes),
            cancelText = stringResource(id = R.string.main_delete_confirm_no),
            onConfirm = actions.onDeleteListConfirm,
            onDismiss = actions.onDeleteListDismiss,
        )
    }

    uiState.renameListTarget?.let { targetList ->
        RenameShoppingListDialog(
            listName = uiState.renameListName,
            isRenamingList = uiState.isRenamingList,
            actions = actions,
        )
    }

    when (val iconPickerState = uiState.iconPickerState) {
        IconPickerState.Hidden -> Unit
        is IconPickerState.Visible -> {
            ShoppingListIconPickerBottomSheet(
                isUpdatingIcon = iconPickerState.isUpdating,
                isErrorVisible = iconPickerState.isErrorVisible,
                actions = actions,
            )
        }
    }
}

@Composable
private fun BoxScope.MainContent(
    contentState: MainContentState,
    isDarkTheme: Boolean,
    isSearching: Boolean,
    scrollToShoppingListId: Long?,
    selectedListId: Long?,
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
            if (isSearching && contentState.shoppingLists.isEmpty()) {
                SearchEmptyState(
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 104.dp),
                )
            } else {
                ShoppingListsContent(
                    shoppingLists = contentState.shoppingLists,
                    scrollToShoppingListId = scrollToShoppingListId,
                    onShoppingListIconClick = actions.onShoppingListIconClick,
                    onShoppingListScrollHandled = actions.onShoppingListScrollHandled,
                    onDeleteListClick = actions.onDeleteListClick,
                    onCopyListClick = actions.onCopyListClick,
                    onRenameListClick = actions.onRenameListClick,
                    onListClick = actions.onListClick,
                    isSearching = isSearching,
                    selectedListId = selectedListId,
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
}

@Composable
private fun SearchEmptyState(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Main.emptyStateHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(
                id = R.drawable.illustration_search_screen
            ),
            contentDescription = stringResource(id = R.string.main_search_empty_title),
            modifier = Modifier
                .size(196.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.main_search_empty_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.main_search_empty_subtitle),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}
