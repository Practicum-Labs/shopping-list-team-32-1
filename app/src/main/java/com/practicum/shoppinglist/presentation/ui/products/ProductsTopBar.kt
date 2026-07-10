@file:Suppress("MatchingDeclarationName")

package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.colors

data class TopBarActions(
    val onRename: () -> Unit,
    val onDelete: () -> Unit,
    val onClearBought: () -> Unit,
    val onSortAlphabetically: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsTopBar(
    title: String,
    onBack: () -> Unit,
    actions: TopBarActions,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val tintColor = if (enabled) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f)
    }
    val moreVertBaseColor = MaterialTheme.colors.productMoreVert
    val moreVertTintColor = if (enabled) {
        moreVertBaseColor
    } else {
        moreVertBaseColor.copy(alpha = 0.38f)
    }
    TopAppBar(
        title = {
            Text(
                text = title,
                color = tintColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack, enabled = enabled) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.products_back_content_description),
                    tint = tintColor
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }, enabled = enabled) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.products_menu_content_description),
                    tint = moreVertTintColor
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colors.addListDialogSurface)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.products_menu_rename),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        actions.onRename()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.products_menu_sort_alphabetically),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        actions.onSortAlphabetically()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.products_menu_clear_bought),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        actions.onClearBought()
                        expanded = false
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.products_menu_delete),
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        actions.onDelete()
                        expanded = false
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
