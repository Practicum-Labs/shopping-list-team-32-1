package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
fun BottomSheetContent(
    name: String,
    onNameChange: (String) -> Unit,
    qtyStr: String,
    onQtyChange: (String) -> Unit,
    unit: String,
    onUnitChange: (String) -> Unit,
    suggestions: List<String>,
    onQueryChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isSaveEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val quantityDouble = qtyStr.replace(',', '.').toDoubleOrNull() ?: 1.0
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val sheetShape = RoundedCornerShape(
        topStart = Dimens.Products.sheetCornerRadius,
        topEnd = Dimens.Products.sheetCornerRadius
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.iconPickerSheetSurface,
                    shape = sheetShape
                )
                .border(
                    width = Dimens.Products.sheetBorderWidth,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    shape = sheetShape
                )
                .navigationBarsPadding()
                .padding(horizontal = Dimens.Products.sheetHorizontalPadding)
                .padding(
                    bottom = Dimens.Products.sheetBottomPadding,
                    top = Dimens.Products.sheetTopPadding
                )
        ) {
            // Drag handle representation
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = Dimens.Products.dragHandleVerticalPadding)
                    .size(
                        width = Dimens.Products.dragHandleWidth,
                        height = Dimens.Products.dragHandleHeight
                    )
            )

            Spacer(modifier = Modifier.height(Dimens.Products.sheetTopPadding))

            ProductNameInputField(
                name = name,
                onNameChange = onNameChange,
                suggestions = suggestions,
                onQueryChange = onQueryChange,
                focusRequester = focusRequester
            )

            Spacer(modifier = Modifier.height(Dimens.Products.verticalSpacing))

            QuantityAndUnitSelectors(
                qtyStr = qtyStr,
                onQtyChange = onQtyChange,
                unit = unit,
                onUnitChange = onUnitChange,
                quantityDouble = quantityDouble
            )
        }

        // The FAB checkmark button floating exactly 32.dp above the sheet top edge
        FloatingActionButton(
            onClick = {
                if (isSaveEnabled) {
                    onSaveClick()
                }
            },
            containerColor = if (isSaveEnabled) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSaveEnabled) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            },
            shape = RoundedCornerShape(Dimens.Products.sheetHorizontalPadding),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = Dimens.Products.sheetHorizontalPadding)
                .offset(y = Dimens.Products.fabOffset)
                .size(Dimens.Products.fabSize)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.products_save_content_description),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
