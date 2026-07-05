package com.practicum.shoppinglist.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.shoppinglist.presentation.theme.Motion
import com.practicum.shoppinglist.presentation.ui.auth.login.LoginRoute
import com.practicum.shoppinglist.presentation.ui.auth.recovery.RecoveryRoute
import com.practicum.shoppinglist.presentation.ui.auth.register.RegisterRoute
import com.practicum.shoppinglist.presentation.ui.main.MainRoute
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingDestination
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingScreen
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingViewModel
import org.koin.androidx.compose.koinViewModel

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
    }
}

private fun NavGraphBuilder.onboardingRoute(
    navController: NavHostController,
    isDarkTheme: Boolean,
) {
    composable(
        route = ONBOARDING_ROUTE,
        exitTransition = {
            if (targetState.destination.route == MAIN_ROUTE) {
                onboardingExitTransition()
            } else {
                ExitTransition.None
            }
        },
    ) {
        val viewModel: OnboardingViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.destination) {
            uiState.destination?.let { destination ->
                navController.navigate(destination.toRoute()) {
                    popUpTo(ONBOARDING_ROUTE) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }

        OnboardingScreen(isDarkTheme = isDarkTheme)
    }
}

private fun NavGraphBuilder.loginRoute(navController: NavHostController) {
    composable(
        route = LOGIN_ROUTE,
        exitTransition = {
            if (targetState.destination.route == MAIN_ROUTE) {
                mainExitTransition()
            } else {
                ExitTransition.None
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
        exitTransition = {
            if (targetState.destination.route == MAIN_ROUTE) {
                mainExitTransition()
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
    composable(route = RECOVERY_ROUTE) {
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
        )
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
    return this == LOGIN_ROUTE || this == REGISTER_ROUTE
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
