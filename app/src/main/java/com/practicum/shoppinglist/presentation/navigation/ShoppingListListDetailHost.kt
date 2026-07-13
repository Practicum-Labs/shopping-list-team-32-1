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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    val detailNavController = rememberNavController()

    BackHandler(enabled = navigator.canNavigateBack()) {
        coroutineScope.launch { navigator.navigateBack() }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        listPane = {
            AnimatedPane {
                ListDetailListPane(
                    isDarkTheme = isDarkTheme,
                    isTwoPane = isTwoPane,
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
                    onBack = { coroutineScope.launch { navigator.navigateBack() } },
                )
            }
        },
    )
}

private fun syncDetailDestination(navController: NavHostController, targetContentKey: Long?) {
    val targetRoute = if (targetContentKey != null) productsRoutePath(targetContentKey) else DETAIL_EMPTY_ROUTE
    val currentRoute = navController.currentDestination?.route
    if (currentRoute == targetRoute) {
        return
    }
    val popUpToId = navController.currentDestination?.id ?: navController.graph.startDestinationId
    navController.navigate(targetRoute) {
        popUpTo(popUpToId) { inclusive = true }
    }
}

@Composable
private fun ListDetailListPane(
    isDarkTheme: Boolean,
    isTwoPane: Boolean,
    onThemeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        MainRoute(
            isDarkTheme = isDarkTheme,
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
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DETAIL_EMPTY_ROUTE,
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
    LaunchedEffect(contentKey) {
        syncDetailDestination(navController, contentKey)
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
