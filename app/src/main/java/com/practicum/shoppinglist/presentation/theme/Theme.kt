package com.practicum.shoppinglist.presentation.theme

import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.AttrRes
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.practicum.shoppinglist.R

@Composable
fun Theme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val themedContext = remember(context, darkTheme) {
        ContextThemeWrapper(
            context,
            if (darkTheme) {
                R.style.Theme_Shoppinglist_Dark
            } else {
                R.style.Theme_Shoppinglist_Light
            },
        )
    }

    CompositionLocalProvider(LocalContext provides themedContext) {
        CompositionLocalProvider(LocalColors provides colors()) {
            MaterialTheme(
                colorScheme = shoppingColorScheme(darkTheme = darkTheme),
                typography = AppTypography,
                content = content,
            )
        }
    }
}

data class Colors(
    val addListDialogSurface: Color,
    val addListDialogLabelContainer: Color,
    val addListDialogAccent: Color,
    val addListDialogIcon: Color,
    val addListDialogTitle: Color,
    val addListDialogPlaceholder: Color,
    val iconPickerSheetSurface: Color,
    val iconPickerItemContainer: Color,
    val listItemSurface: Color,
    val listItemTitle: Color,
    val swipeActionBackground: Color,
    val confirmDialogCancelBackground: Color,
    val confirmDialogDeleteBackground: Color,
    val confirmDialogCancelText: Color,
    val confirmDialogDeleteText: Color,
)

val MaterialTheme.colors: Colors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

private val LocalColors = staticCompositionLocalOf<Colors> {
    error("Shopping list colors are not provided")
}

@Composable
@ReadOnlyComposable
private fun colors(): Colors {
    return Colors(
        addListDialogSurface = colorAttr(R.attr.shoppingColorAddListDialogSurface),
        addListDialogLabelContainer = colorAttr(R.attr.shoppingColorAddListDialogLabelContainer),
        addListDialogAccent = colorAttr(R.attr.shoppingColorAddListDialogAccent),
        addListDialogIcon = colorAttr(R.attr.shoppingColorAddListDialogIcon),
        addListDialogTitle = colorAttr(R.attr.shoppingColorAddListDialogTitle),
        addListDialogPlaceholder = colorAttr(R.attr.shoppingColorAddListDialogPlaceholder),
        iconPickerSheetSurface = colorAttr(R.attr.shoppingColorIconPickerSheetSurface),
        iconPickerItemContainer = colorAttr(R.attr.shoppingColorIconPickerItemContainer),
        listItemSurface = colorAttr(R.attr.shoppingColorListItemSurface),
        listItemTitle = colorAttr(R.attr.shoppingColorListItemTitle),
        swipeActionBackground = colorAttr(R.attr.shoppingColorSwipeActionBackground),
        confirmDialogCancelBackground = colorAttr(R.attr.shoppingColorConfirmDialogCancelBackground),
        confirmDialogDeleteBackground = colorAttr(R.attr.shoppingColorConfirmDialogDeleteBackground),
        confirmDialogCancelText = colorAttr(R.attr.shoppingColorConfirmDialogCancelText),
        confirmDialogDeleteText = colorAttr(R.attr.shoppingColorConfirmDialogDeleteText),
    )
}

@Composable
@ReadOnlyComposable
private fun shoppingColorScheme(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = colorAttr(R.attr.shoppingColorPrimary),
            onPrimary = colorAttr(R.attr.shoppingColorOnPrimary),
            primaryContainer = colorAttr(R.attr.shoppingColorPrimaryContainer),
            onPrimaryContainer = colorAttr(R.attr.shoppingColorOnPrimaryContainer),
            secondary = colorAttr(R.attr.shoppingColorSecondary),
            onSecondary = colorAttr(R.attr.shoppingColorOnSecondary),
            background = colorAttr(R.attr.shoppingColorBackground),
            onBackground = colorAttr(R.attr.shoppingColorOnBackground),
            surface = colorAttr(R.attr.shoppingColorSurface),
            onSurface = colorAttr(R.attr.shoppingColorOnSurface),
            surfaceVariant = colorAttr(R.attr.shoppingColorSurfaceVariant),
            onSurfaceVariant = colorAttr(R.attr.shoppingColorOnSurfaceVariant),
            outline = colorAttr(R.attr.shoppingColorOutline),
            error = colorAttr(R.attr.shoppingColorError),
            onError = colorAttr(R.attr.shoppingColorOnError),
        )
    } else {
        lightColorScheme(
            primary = colorAttr(R.attr.shoppingColorPrimary),
            onPrimary = colorAttr(R.attr.shoppingColorOnPrimary),
            primaryContainer = colorAttr(R.attr.shoppingColorPrimaryContainer),
            onPrimaryContainer = colorAttr(R.attr.shoppingColorOnPrimaryContainer),
            secondary = colorAttr(R.attr.shoppingColorSecondary),
            onSecondary = colorAttr(R.attr.shoppingColorOnSecondary),
            background = colorAttr(R.attr.shoppingColorBackground),
            onBackground = colorAttr(R.attr.shoppingColorOnBackground),
            surface = colorAttr(R.attr.shoppingColorSurface),
            onSurface = colorAttr(R.attr.shoppingColorOnSurface),
            surfaceVariant = colorAttr(R.attr.shoppingColorSurfaceVariant),
            onSurfaceVariant = colorAttr(R.attr.shoppingColorOnSurfaceVariant),
            outline = colorAttr(R.attr.shoppingColorOutline),
            error = colorAttr(R.attr.shoppingColorError),
            onError = colorAttr(R.attr.shoppingColorOnError),
        )
    }
}

@Composable
@ReadOnlyComposable
fun colorAttr(@AttrRes attrRes: Int): Color {
    val context = LocalContext.current
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attrRes, typedValue, true)
    return Color(typedValue.data)
}
