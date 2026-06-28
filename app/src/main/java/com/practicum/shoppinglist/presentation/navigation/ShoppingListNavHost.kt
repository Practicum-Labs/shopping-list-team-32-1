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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.shoppinglist.presentation.theme.Motion
import com.practicum.shoppinglist.presentation.ui.main.MainRoute
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingScreen
import kotlinx.coroutines.delay

@Composable
fun ShoppingListNavHost(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ShoppingListRoute.Onboarding,
        modifier = modifier,
    ) {
        composable(
            route = ShoppingListRoute.Onboarding,
            exitTransition = {
                if (targetState.destination.route == ShoppingListRoute.Main) {
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = Motion.Navigation.onboardingExitDurationMillis,
                        ),
                    ) + scaleOut(
                        animationSpec = tween(
                            durationMillis = Motion.Navigation.onboardingExitDurationMillis,
                        ),
                        targetScale = Motion.Navigation.onboardingExitTargetScale,
                    )
                } else {
                    ExitTransition.None
                }
            },
        ) {
            LaunchedEffect(Unit) {
                delay(Motion.Navigation.loadingScreenDelayMillis)
                navController.navigate(ShoppingListRoute.Main) {
                    popUpTo(ShoppingListRoute.Onboarding) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            OnboardingScreen(isDarkTheme = isDarkTheme)
        }

        composable(
            route = ShoppingListRoute.Main,
            enterTransition = {
                if (initialState.destination.route == ShoppingListRoute.Onboarding) {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = Motion.Navigation.mainEnterDurationMillis,
                        ),
                    ) + scaleIn(
                        animationSpec = tween(
                            durationMillis = Motion.Navigation.mainEnterDurationMillis,
                        ),
                        initialScale = Motion.Navigation.mainEnterInitialScale,
                    )
                } else {
                    EnterTransition.None
                }
            },
        ) {
            MainRoute(
                isDarkTheme = isDarkTheme,
                onThemeClick = onThemeClick,
            )
        }
    }
}

private object ShoppingListRoute {
    const val Onboarding = "onboarding"
    const val Main = "main"
}
