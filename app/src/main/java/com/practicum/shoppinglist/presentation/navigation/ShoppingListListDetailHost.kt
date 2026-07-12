package com.practicum.shoppinglist.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.ui.main.MainRoute
import com.practicum.shoppinglist.presentation.ui.products.ProductsRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ShoppingListListDetailHost(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val coroutineScope = rememberCoroutineScope()
    val isTwoPane = navigator.scaffoldDirective.maxHorizontalPartitions > 1

    BackHandler(enabled = navigator.canNavigateBack()) {
        coroutineScope.launch { navigator.navigateBack() }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        listPane = {
            AnimatedPane {
                Box(modifier = Modifier.fillMaxSize()) {
                    MainRoute(
                        isDarkTheme = isDarkTheme,
                        onThemeClick = onThemeClick,
                        onLogoutClick = onLogoutClick,
                        onListClick = { listId ->
                            coroutineScope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, listId)
                            }
                        },
                    )
                    if (isTwoPane) {
                        VerticalDivider(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                val listId = navigator.currentDestination?.contentKey
                if (listId != null) {
                    ProductsRoute(
                        listId = listId,
                        onBack = { coroutineScope.launch { navigator.navigateBack() } },
                    )
                } else {
                    ListDetailEmptyState()
                }
            }
        },
    )
}

@Composable
private fun ListDetailEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ListDetail.emptyStateHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ListAlt,
            contentDescription = null,
            modifier = Modifier
                .size(Dimens.ListDetail.emptyStateIconSize)
                .padding(bottom = Dimens.ListDetail.emptyStateTitleTopPadding),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.list_detail_empty_state_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(R.string.list_detail_empty_state_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = Dimens.ListDetail.emptyStateSubtitleTopPadding),
        )
    }
}
