package com.practicum.shoppinglist.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
internal fun MenuItemRow(
    icon: Painter,
    iconContentDescription: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    val resolvedBackground = if (backgroundColor == Color.Unspecified) {
        MaterialTheme.colors.menuSheetSurface
    } else {
        backgroundColor
    }
    Surface(
        onClick = onClick,
        color = resolvedBackground,
        contentColor = MaterialTheme.colors.menuSheetSurface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = Dimens.Main.menuSheetItemHeight)
                .padding(
                    horizontal = Dimens.Main.menuSheetItemHorizontalPadding,
                    vertical = if (subtitle != null) Dimens.Main.menuSheetSortItemVerticalPadding else 0.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = iconContentDescription,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(Dimens.Main.menuSheetItemIconSize),
            )
            Spacer(modifier = Modifier.width(Dimens.Main.menuSheetItemIconEndPadding))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colors.menuSheetSortLabel,
                    )
                }
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}
