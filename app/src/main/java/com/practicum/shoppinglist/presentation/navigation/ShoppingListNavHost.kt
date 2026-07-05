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
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.shoppinglist.domain.usecase.auth.CheckAuthUseCase
import com.practicum.shoppinglist.presentation.theme.Motion
import com.practicum.shoppinglist.presentation.ui.auth.login.LoginRoute
import com.practicum.shoppinglist.presentation.ui.auth.recovery.RecoveryRoute
import com.practicum.shoppinglist.presentation.ui.auth.register.RegisterRoute
import com.practicum.shoppinglist.presentation.ui.main.MainRoute
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingScreen
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun ShoppingListNavHost(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val checkAuthUseCase: CheckAuthUseCase = koinInject()

    NavHost(
        navController = navController,
        startDestination = ShoppingListRoute.Onboarding,
        modifier = modifier,
    ) {
        onboardingRoute(navController, checkAuthUseCase, isDarkTheme)
        loginRoute(navController)
        registerRoute(navController)
        recoveryRoute(navController)
        mainRoute(navController, isDarkTheme, onThemeClick)
    }
}

private fun NavGraphBuilder.onboardingRoute(
    navController: NavHostController,
    checkAuthUseCase: CheckAuthUseCase,
    isDarkTheme: Boolean,
) {
    composable(
        route = ShoppingListRoute.Onboarding,
        exitTransition = {
            if (targetState.destination.route == ShoppingListRoute.Main) {
                onboardingExitTransition()
            } else {
                ExitTransition.None
            }
        },
    ) {
        LaunchedEffect(Unit) {
            val authCheck = async { checkAuthUseCase() }
            delay(Motion.Navigation.loadingScreenDelayMillis)
            val nextRoute = if (authCheck.await()) {
                ShoppingListRoute.Main
            } else {
                ShoppingListRoute.Login
            }
            navController.navigate(nextRoute) {
                popUpTo(ShoppingListRoute.Onboarding) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        OnboardingScreen(isDarkTheme = isDarkTheme)
    }
}

private fun NavGraphBuilder.loginRoute(navController: NavHostController) {
    composable(
        route = ShoppingListRoute.Login,
        exitTransition = {
            if (targetState.destination.route == ShoppingListRoute.Main) {
                mainExitTransition()
            } else {
                ExitTransition.None
            }
        },
    ) {
        LoginRoute(
            onLoginSuccess = { navController.navigateToMainFromLogin() },
            onRegisterClick = { navController.navigate(ShoppingListRoute.Register) },
            onRecoveryClick = { navController.navigate(ShoppingListRoute.Recovery) },
        )
    }
}

private fun NavGraphBuilder.registerRoute(navController: NavHostController) {
    composable(
        route = ShoppingListRoute.Register,
        exitTransition = {
            if (targetState.destination.route == ShoppingListRoute.Main) {
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
    composable(route = ShoppingListRoute.Recovery) {
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
        route = ShoppingListRoute.Main,
        enterTransition = {
            if (
                initialState.destination.route == ShoppingListRoute.Onboarding ||
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
    navigate(ShoppingListRoute.Main) {
        popUpTo(ShoppingListRoute.Login) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateToLoginFromMain() {
    navigate(ShoppingListRoute.Login) {
        popUpTo(ShoppingListRoute.Main) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateBackToLogin() {
    popBackStack(
        route = ShoppingListRoute.Login,
        inclusive = false,
    )
}

private object ShoppingListRoute {
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val Register = "register"
    const val Recovery = "recovery"
    const val Main = "main"
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
    return this == ShoppingListRoute.Login || this == ShoppingListRoute.Register
}
