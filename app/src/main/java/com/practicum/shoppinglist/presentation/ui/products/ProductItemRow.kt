package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@Suppress("CognitiveComplexMethod")
@Composable
fun ProductItemRow(
    item: ShoppingItem,
    onToggleBought: (ShoppingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = if (item.isBought) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onBackground
    }
    val textDecoration = if (item.isBought) TextDecoration.LineThrough else null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Dimens.Products.listItemVerticalPadding,
                    horizontal = Dimens.Products.listItemHorizontalPadding
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductCheckbox(
                checked = item.isBought,
                onCheckedChange = { onToggleBought(item) }
            )
            Spacer(modifier = Modifier.width(Dimens.Products.itemSpacing))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    textDecoration = textDecoration
                )
                if (item.quantity > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    val qtyStr = if (item.quantity % 1.0 == 0.0) {
                        item.quantity.toInt().toString()
                    } else {
                        item.quantity.toString()
                    }
                    Text(
                        text = "$qtyStr ${item.unit}".trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colors.productDivider)
    }
}
