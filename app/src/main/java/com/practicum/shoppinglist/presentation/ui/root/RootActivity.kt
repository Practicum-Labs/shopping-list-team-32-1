package com.practicum.shoppinglist.presentation.ui.root

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.shoppinglist.presentation.navigation.ShoppingListNavHost
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.theme.ThemeController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShoppingListRoot()
        }
    }
}

@Composable
private fun ShoppingListRoot() {
    val themeController: ThemeController = koinInject()
    val coroutineScope = rememberCoroutineScope()
    val systemDarkTheme = isSystemInDarkTheme()
    val savedDarkTheme by themeController.savedDarkTheme.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val isDarkTheme = savedDarkTheme ?: systemDarkTheme

    ShoppingListRootContent(
        isDarkTheme = isDarkTheme,
        onThemeClick = {
            coroutineScope.launch {
                themeController.setDarkTheme(isDarkTheme = !isDarkTheme)
            }
        },
    )
}

@Composable
private fun ShoppingListRootContent(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
) {
    SyncSystemBars(isDarkTheme = isDarkTheme)

    Theme(darkTheme = isDarkTheme) {
        ShoppingListNavHost(
            isDarkTheme = isDarkTheme,
            onThemeClick = onThemeClick,
        )
    }
}

@Composable
private fun SyncSystemBars(isDarkTheme: Boolean) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListRootPreview() {
    ShoppingListRootContent(
        isDarkTheme = false,
        onThemeClick = {},
    )
}
