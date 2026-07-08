package com.practicum.shoppinglist.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practicum.shoppinglist.presentation.theme.Motion
import com.practicum.shoppinglist.presentation.ui.auth.login.LoginRoute
import com.practicum.shoppinglist.presentation.ui.auth.recovery.RecoveryRoute
import com.practicum.shoppinglist.presentation.ui.auth.register.RegisterRoute
import com.practicum.shoppinglist.presentation.ui.main.MainRoute
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingDestination
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingRoute
import com.practicum.shoppinglist.presentation.ui.products.ProductsRoute

@Composable
fun ShoppingListNavHost(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ONBOARDING_ROUTE,
        modifier = modifier,
    ) {
        onboardingRoute(navController, isDarkTheme)
        loginRoute(navController)
        registerRoute(navController)
        recoveryRoute(navController)
        mainRoute(navController, isDarkTheme, onThemeClick)
        productsRoute(navController)
    }
}

private fun NavGraphBuilder.onboardingRoute(
    navController: NavHostController,
    isDarkTheme: Boolean,
) {
    composable(
        route = ONBOARDING_ROUTE,
        exitTransition = {
            if (
                targetState.destination.route == MAIN_ROUTE ||
                targetState.destination.route == LOGIN_ROUTE
            ) {
                onboardingExitTransition()
            } else {
                ExitTransition.None
            }
        },
    ) {
        OnboardingRoute(
            isDarkTheme = isDarkTheme,
            onNavigate = { destination ->
                navController.navigate(destination.toRoute()) {
                    popUpTo(ONBOARDING_ROUTE) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
        )
    }
}

private fun NavGraphBuilder.loginRoute(navController: NavHostController) {
    composable(
        route = LOGIN_ROUTE,
        enterTransition = {
            if (
                initialState.destination.route == ONBOARDING_ROUTE ||
                initialState.destination.route.isAuthRoute()
            ) {
                authEnterTransition()
            } else {
                EnterTransition.None
            }
        },
        exitTransition = {
            if (targetState.destination.route == MAIN_ROUTE) {
                mainExitTransition()
            } else if (targetState.destination.route.isAuthRoute()) {
                authExitTransition()
            } else {
                ExitTransition.None
            }
        },
        popEnterTransition = {
            if (initialState.destination.route.isAuthRoute()) {
                authEnterTransition()
            } else {
                EnterTransition.None
            }
        },
    ) {
        LoginRoute(
            onLoginSuccess = { navController.navigateToMainFromLogin() },
            onRegisterClick = { navController.navigate(REGISTER_ROUTE) },
            onRecoveryClick = { navController.navigate(RECOVERY_ROUTE) },
        )
    }
}

private fun NavGraphBuilder.registerRoute(navController: NavHostController) {
    composable(
        route = REGISTER_ROUTE,
        enterTransition = {
            if (initialState.destination.route.isAuthRoute()) {
                authEnterTransition()
            } else {
                EnterTransition.None
            }
        },
        exitTransition = {
            if (targetState.destination.route == MAIN_ROUTE) {
                mainExitTransition()
            } else if (targetState.destination.route.isAuthRoute()) {
                authExitTransition()
            } else {
                ExitTransition.None
            }
        },
        popExitTransition = {
            if (targetState.destination.route.isAuthRoute()) {
                authExitTransition()
            } else {
                ExitTransition.None
            }
        },
    ) {
        RegisterRoute(
            onBackClick = { navController.navigateBackToLogin() },
            onRegisterSuccess = { navController.navigateToMainFromLogin() },
        )
    }
}

private fun NavGraphBuilder.recoveryRoute(navController: NavHostController) {
    composable(
        route = RECOVERY_ROUTE,
        enterTransition = {
            if (initialState.destination.route.isAuthRoute()) {
                authEnterTransition()
            } else {
                EnterTransition.None
            }
        },
        popExitTransition = {
            if (targetState.destination.route.isAuthRoute()) {
                authExitTransition()
            } else {
                ExitTransition.None
            }
        },
    ) {
        RecoveryRoute(
            onBackClick = { navController.navigateBackToLogin() },
        )
    }
}

private fun NavGraphBuilder.mainRoute(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
) {
    composable(
        route = MAIN_ROUTE,
        enterTransition = {
            if (
                initialState.destination.route == ONBOARDING_ROUTE ||
                initialState.destination.route.isAuthRoute()
            ) {
                mainEnterTransition()
            } else {
                EnterTransition.None
            }
        },
    ) {
        MainRoute(
            isDarkTheme = isDarkTheme,
            onThemeClick = onThemeClick,
            onLogoutClick = { navController.navigateToLoginFromMain() },
            onListClick = { listId ->
                navController.navigate(productsRoutePath(listId))
            }
        )
    }
}

private fun NavGraphBuilder.productsRoute(navController: NavHostController) {
    composable(
        route = PRODUCTS_ROUTE,
        arguments = listOf(
            navArgument(PRODUCTS_ROUTE_ARG_LIST_ID) { type = NavType.LongType }
        )
    ) { backStackEntry ->
        val listId = backStackEntry.arguments?.getLong(PRODUCTS_ROUTE_ARG_LIST_ID)
        if (listId != null) {
            ProductsRoute(
                listId = listId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun NavHostController.navigateToMainFromLogin() {
    navigate(MAIN_ROUTE) {
        popUpTo(LOGIN_ROUTE) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateToLoginFromMain() {
    navigate(LOGIN_ROUTE) {
        popUpTo(MAIN_ROUTE) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateBackToLogin() {
    popBackStack(
        route = LOGIN_ROUTE,
        inclusive = false,
    )
}

private fun onboardingExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = Motion.Navigation.onboardingExitDurationMillis,
        ),
    ) + scaleOut(
        animationSpec = tween(
            durationMillis = Motion.Navigation.onboardingExitDurationMillis,
        ),
        targetScale = Motion.Navigation.onboardingExitTargetScale,
    )
}

private fun mainEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = Motion.Navigation.mainEnterDurationMillis,
        ),
    ) + scaleIn(
        animationSpec = tween(
            durationMillis = Motion.Navigation.mainEnterDurationMillis,
        ),
        initialScale = Motion.Navigation.mainEnterInitialScale,
    )
}

private fun mainExitTransition(): ExitTransition {
    return authExitTransition()
}

private fun authEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = Motion.Navigation.authExitDurationMillis,
        ),
    ) + scaleIn(
        animationSpec = tween(
            durationMillis = Motion.Navigation.authExitDurationMillis,
        ),
        initialScale = Motion.Navigation.mainEnterInitialScale,
    )
}

private fun authExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = Motion.Navigation.authExitDurationMillis,
        ),
    ) + scaleOut(
        animationSpec = tween(
            durationMillis = Motion.Navigation.authExitDurationMillis,
        ),
        targetScale = Motion.Navigation.authExitTargetScale,
    )
}

private fun String?.isAuthRoute(): Boolean {
    return this == LOGIN_ROUTE || this == REGISTER_ROUTE || this == RECOVERY_ROUTE
}

private fun OnboardingDestination.toRoute(): String {
    return when (this) {
        OnboardingDestination.Main -> MAIN_ROUTE
        OnboardingDestination.Login -> LOGIN_ROUTE
    }
}

private const val ONBOARDING_ROUTE = "onboarding"
private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val RECOVERY_ROUTE = "recovery"
private const val MAIN_ROUTE = "main"
private const val PRODUCTS_ROUTE_ARG_LIST_ID = "listId"
private const val PRODUCTS_ROUTE = "products/{$PRODUCTS_ROUTE_ARG_LIST_ID}"
private fun productsRoutePath(listId: Long) = "products/$listId"
