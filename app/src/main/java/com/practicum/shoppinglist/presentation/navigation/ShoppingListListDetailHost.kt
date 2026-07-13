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
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.ui.main.MainRoute
import com.practicum.shoppinglist.presentation.ui.main.MainViewModel
import com.practicum.shoppinglist.presentation.ui.products.ProductsRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
    val detailNavController = rememberNavController()

    val mainViewModel: MainViewModel = koinViewModel()
    val existingListIds by mainViewModel.existingListIds.collectAsStateWithLifecycle()

    BackHandler(enabled = navigator.canNavigateBack()) {
        coroutineScope.launch { navigator.navigateBack() }
    }

    DiscardDeletedSelection(navigator = navigator, existingListIds = existingListIds)

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        listPane = {
            AnimatedPane {
                ListDetailListPane(
                    isDarkTheme = isDarkTheme,
                    isTwoPane = isTwoPane,
                    selectedListId = navigator.currentDestination?.contentKey?.takeIf { isTwoPane },
                    onThemeClick = onThemeClick,
                    onLogoutClick = onLogoutClick,
                    onListClick = { listId ->
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, listId)
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                ListDetailDetailPane(
                    navController = detailNavController,
                    contentKey = navigator.currentDestination?.contentKey,
                    isTwoPane = isTwoPane,
                    onBack = { coroutineScope.launch { navigator.navigateBack() } },
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun DiscardDeletedSelection(
    navigator: ThreePaneScaffoldNavigator<Long>,
    existingListIds: Set<Long>?,
) {
    LaunchedEffect(existingListIds, navigator.currentDestination?.contentKey) {
        if (existingListIds != null) {
            while (true) {
                val selectedListId = navigator.currentDestination?.contentKey
                if (selectedListId == null || selectedListId in existingListIds) {
                    break
                }
                navigator.navigateBack(BackNavigationBehavior.PopLatest)
            }
        }
    }
}

private fun syncDetailDestination(navController: NavHostController, targetContentKey: Long?) {
    val targetRoute = if (targetContentKey != null) productsRoutePath(targetContentKey) else DETAIL_EMPTY_ROUTE
    if (navController.currentDetailRoute() == targetRoute) {
        return
    }
    val popUpToId = navController.currentDestination?.id ?: navController.graph.startDestinationId
    navController.navigate(targetRoute) {
        popUpTo(popUpToId) { inclusive = true }
    }
}

private fun NavHostController.currentDetailRoute(): String? {
    val entry = currentBackStackEntry ?: return null
    return when (entry.destination.route) {
        PRODUCTS_ROUTE -> entry.arguments
            ?.getLong(PRODUCTS_ROUTE_ARG_LIST_ID)
            ?.let(::productsRoutePath)

        else -> entry.destination.route
    }
}

@Composable
private fun ListDetailListPane(
    isDarkTheme: Boolean,
    isTwoPane: Boolean,
    selectedListId: Long?,
    onThemeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        MainRoute(
            isDarkTheme = isDarkTheme,
            selectedListId = selectedListId,
            onThemeClick = onThemeClick,
            onLogoutClick = onLogoutClick,
            onListClick = onListClick,
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

@Composable
private fun ListDetailDetailPane(
    navController: NavHostController,
    contentKey: Long?,
    isTwoPane: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Deliberately unkeyed: this is the destination the pane opens on. In single-pane mode the pane
    // is composed only while a list is selected, so the NavHost lands straight on that list instead
    // of rendering the placeholder and then animating away from it.
    val startDestination = remember {
        if (contentKey != null) productsRoutePath(contentKey) else DETAIL_EMPTY_ROUTE
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(DETAIL_EMPTY_ROUTE) {
            ListDetailEmptyState()
        }
        composable(
            route = PRODUCTS_ROUTE,
            arguments = listOf(navArgument(PRODUCTS_ROUTE_ARG_LIST_ID) { type = NavType.LongType }),
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong(PRODUCTS_ROUTE_ARG_LIST_ID)
            if (listId != null) {
                ProductsRoute(listId = listId, onBack = onBack)
            }
        }
    }

    // Must stay inside the detail pane: in single-pane mode AnimatedPane composes this content
    // only once the detail pane is shown, and NavHost sets the graph while composing. An effect
    // hoisted to the scaffold would run before the graph exists and never retry.
    LaunchedEffect(contentKey, isTwoPane) {
        // Single-pane deselection means the pane itself is animating out; swapping in the
        // placeholder would show it during that exit.
        val isPaneClosing = contentKey == null && !isTwoPane
        if (!isPaneClosing) {
            syncDetailDestination(navController, contentKey)
        }
    }
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

private const val DETAIL_EMPTY_ROUTE = "products_empty"
private const val PRODUCTS_ROUTE_ARG_LIST_ID = "listId"
private const val PRODUCTS_ROUTE = "products/{$PRODUCTS_ROUTE_ARG_LIST_ID}"
private fun productsRoutePath(listId: Long) = "products/$listId"
