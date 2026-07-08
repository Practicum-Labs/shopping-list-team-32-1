package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors

@Composable
fun ProductNameInputField(
    name: String,
    onNameChange: (String) -> Unit,
    suggestions: List<String>,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isSuggestionsExpanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val labelBg = if (isFocused || name.isNotEmpty()) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colors.iconPickerSheetSurface
    }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= MAX_PRODUCT_NAME_LENGTH) {
                    onNameChange(it)
                    onQueryChange(it)
                    isSuggestionsExpanded = it.isNotEmpty()
                }
            },
            label = {
                Text(
                    text = stringResource(R.string.products_input_name_label),
                    modifier = Modifier
                        .background(labelBg)
                        .padding(horizontal = 4.dp)
                )
            },
            placeholder = { Text(stringResource(R.string.products_input_name_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colors.addListDialogAccent,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colors.addListDialogAccent,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = MaterialTheme.colors.addListDialogAccent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        DropdownMenu(
            expanded = isSuggestionsExpanded && suggestions.isNotEmpty(),
            onDismissRequest = { isSuggestionsExpanded = false },
            properties = PopupProperties(focusable = false),
            modifier = Modifier
                .background(MaterialTheme.colors.iconPickerSheetSurface)
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion, color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onNameChange(suggestion)
                        isSuggestionsExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun QuantityAndUnitSelectors(
    qtyStr: String,
    onQtyChange: (String) -> Unit,
    unit: String,
    onUnitChange: (String) -> Unit,
    quantityDouble: Double,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val unitFocusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val units = remember { context.resources.getStringArray(R.array.product_units).toList() }

    val filteredUnits = remember(unit, units) {
        if (unit.isEmpty()) {
            units
        } else {
            units.filter { it.contains(unit, ignoreCase = true) }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Products.itemSpacing)
    ) {
        QuantityField(
            qtyStr = qtyStr,
            onQtyChange = onQtyChange
        )

        var isUnitsExpanded by remember { mutableStateOf(false) }
        UnitField(
            unit = unit,
            onUnitChange = onUnitChange,
            filteredUnits = filteredUnits,
            isUnitsExpanded = isUnitsExpanded,
            onExpandedChange = { isUnitsExpanded = it },
            unitFocusRequester = unitFocusRequester,
            focusManager = focusManager
        )

        QuantityAdjusters(
            qtyStr = qtyStr,
            onQtyChange = onQtyChange,
            quantityDouble = quantityDouble
        )
    }
}

@Composable
private fun QuantityField(
    qtyStr: String,
    onQtyChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val labelBg = if (isFocused || qtyStr.isNotEmpty()) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colors.iconPickerSheetSurface
    }

    OutlinedTextField(
        value = qtyStr,
        onValueChange = onQtyChange,
        label = {
            Text(
                text = stringResource(R.string.products_input_qty_label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(labelBg)
                    .padding(horizontal = 4.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .width(Dimens.Products.fieldWidthQuantity)
            .height(Dimens.Products.textFieldHeight)
            .onFocusChanged { isFocused = it.isFocused },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colors.addListDialogAccent,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colors.addListDialogAccent,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colors.addListDialogAccent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}

@Composable
private fun UnitField(
    unit: String,
    onUnitChange: (String) -> Unit,
    filteredUnits: List<String>,
    isUnitsExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    unitFocusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    var isFocused by remember { mutableStateOf(false) }
    val labelBg = if (isFocused || unit.isNotEmpty()) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colors.iconPickerSheetSurface
    }

    Box(modifier = Modifier.width(Dimens.Products.fieldWidthUnit)) {
        OutlinedTextField(
            value = unit,
            onValueChange = {
                onUnitChange(it)
                onExpandedChange(true)
            },
            readOnly = false,
            label = {
                Text(
                    text = stringResource(R.string.products_input_unit_label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .background(labelBg)
                        .padding(horizontal = 4.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.Products.textFieldHeight)
                .focusRequester(unitFocusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        onExpandedChange(true)
                    }
                },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colors.addListDialogAccent,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colors.addListDialogAccent,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        DropdownMenu(
            expanded = isUnitsExpanded && filteredUnits.isNotEmpty(),
            onDismissRequest = {
                onExpandedChange(false)
            },
            properties = PopupProperties(focusable = false),
            modifier = Modifier
                .width(Dimens.Products.fieldWidthUnit)
                .background(MaterialTheme.colors.addListDialogSurface)
        ) {
            filteredUnits.forEach { u ->
                DropdownMenuItem(
                    text = { Text(u, color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onUnitChange(u)
                        onExpandedChange(false)
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

@Suppress("CognitiveComplexMethod")
@Composable
private fun QuantityAdjusters(
    qtyStr: String,
    onQtyChange: (String) -> Unit,
    quantityDouble: Double
) {
    Row(
        modifier = Modifier
            .width(Dimens.Products.quantityAdjustmentContainerWidth)
            .height(Dimens.Products.quantityAdjustmentContainerHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isDecrementEnabled = quantityDouble > 1.0
        Box(
            modifier = Modifier
                .size(Dimens.Products.buttonClickSize)
                .clip(CircleShape)
                .clickable(enabled = isDecrementEnabled) {
                    val current = qtyStr.replace(',', '.').toDoubleOrNull() ?: 1.0
                    if (current > 1.0) {
                        val next = current - 1.0
                        onQtyChange(
                            if (next % 1.0 == 0.0) next.toInt().toString() else next.toString()
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val bgDisabledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            Box(
                modifier = Modifier
                    .size(Dimens.Products.buttonCircleSize)
                    .background(
                        color = if (isDecrementEnabled) {
                            MaterialTheme.colors.iconPickerItemContainer
                        } else {
                            bgDisabledColor
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                val minusColor = if (isDecrementEnabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
                Canvas(modifier = Modifier.size(14.dp)) {
                    val strokeWidth = 2.dp.toPx()
                    val y = size.height / 2
                    drawLine(
                        color = minusColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Butt
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(Dimens.Products.buttonClickSize)
                .clip(CircleShape)
                .clickable {
                    val current = qtyStr.replace(',', '.').toDoubleOrNull() ?: 0.0
                    val next = current + 1.0
                    if (next <= MAX_PRODUCT_QUANTITY) {
                        onQtyChange(
                            if (next % 1.0 == 0.0) next.toInt().toString() else next.toString()
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.Products.buttonCircleSize)
                    .background(MaterialTheme.colors.iconPickerItemContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val plusColor = MaterialTheme.colorScheme.onSurface
                Canvas(modifier = Modifier.size(14.dp)) {
                    val strokeWidth = 2.dp.toPx()
                    val halfWidth = size.width / 2
                    val halfHeight = size.height / 2
                    drawLine(
                        color = plusColor,
                        start = Offset(0f, halfHeight),
                        end = Offset(size.width, halfHeight),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Butt
                    )
                    drawLine(
                        color = plusColor,
                        start = Offset(halfWidth, 0f),
                        end = Offset(halfWidth, size.height),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Butt
                    )
                }
            }
        }
    }
}
