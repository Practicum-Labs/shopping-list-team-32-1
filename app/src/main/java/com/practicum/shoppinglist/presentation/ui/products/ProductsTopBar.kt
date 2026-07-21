@file:Suppress("MatchingDeclarationName")

package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsTopBar(
    title: String,
    onBack: () -> Unit,
    onMenuClick: () -> Unit,
    enabled: Boolean = true
) {
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
            IconButton(onClick = onMenuClick, enabled = enabled) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.products_menu_content_description),
                    tint = moreVertTintColor
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
