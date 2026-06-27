package com.practicum.shoppinglist.presentation.ui.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.ui.main.MainScreenActions

@Composable
fun MainActionsRow(
    isDarkTheme: Boolean,
    actions: MainScreenActions,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Main.actionSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MainActionButton(
            icon = Icons.Outlined.Search,
            contentDescription = stringResource(id = R.string.main_search_content_description),
            onClick = actions.onSearchClick,
        )
        MainActionButton(
            icon = Icons.Outlined.DeleteOutline,
            contentDescription = stringResource(id = R.string.main_delete_content_description),
            onClick = actions.onDeleteClick,
        )
        MainActionButton(
            icon = if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
            contentDescription = stringResource(id = R.string.main_theme_content_description),
            onClick = actions.onThemeClick,
        )
    }
}

@Composable
private fun MainActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(Dimens.Main.actionButtonSize),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimens.Main.actionIconSize),
        )
    }
}
