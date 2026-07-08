package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.theme.isDarkTheme
import com.practicum.shoppinglist.presentation.ui.main.shoppingListIconByName

private const val COLOR_LIGHT_LABEL_BG = 0xFFFFF8F4
private const val COLOR_DARK_LABEL_BG = 0xFF19120C
private const val COLOR_LIGHT_CHECKED_BG = 0xFF845416
private const val COLOR_DARK_CHECKED_BG = 0xFFFFBE77
private const val COLOR_DARK_TICK = 0xFF19120C
private const val COLOR_LIGHT_TICK = 0xFFFFFFFF
private const val COLOR_LIGHT_MORE_VERT = 0xFF50453A
private const val COLOR_DARK_MORE_VERT = 0xFFD9C8B9
private const val COLOR_DIVIDER = 0xFFCAC4D0
private const val MAX_PRODUCT_NAME_LENGTH = 64
private const val MAX_PRODUCT_QUANTITY = 10000.0

data class TopBarActions(
    val onRename: () -> Unit,
    val onDelete: () -> Unit,
    val onClearBought: () -> Unit,
    val onSortAlphabetically: () -> Unit
)

@Composable
fun ProductsScreen(viewModel: ProductsViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val context = LocalContext.current
    val units = remember { context.resources.getStringArray(R.array.product_units).toList() }
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ShoppingItem?>(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showCancelConfirmDialog by remember { mutableStateOf(false) }

    // Input States
    var nameInput by remember { mutableStateOf("") }
    var qtyInput by remember { mutableStateOf("") }
    var unitInput by remember { mutableStateOf("") }

    LaunchedEffect(editingItem) {
        editingItem?.let { item ->
            nameInput = item.name
            qtyInput = item.quantity.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() }
            unitInput = item.unit
        } ?: run {
            nameInput = ""
            qtyInput = ""
            unitInput = ""
        }
    }

    LaunchedEffect(showAddDialog) {
        if (showAddDialog) {
            nameInput = ""
            qtyInput = ""
            unitInput = ""
        }
    }

    LaunchedEffect(state.listDeleted) {
        if (state.listDeleted) onBack()
    }

    val isSheetOpen = showAddDialog || editingItem != null

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                ProductsTopBar(
                    title = state.list?.name ?: "Продукты",
                    onBack = onBack,
                    actions = TopBarActions(
                        onRename = { showRenameDialog = true },
                        onDelete = { showDeleteConfirmDialog = true },
                        onClearBought = { viewModel.clearBought() },
                        onSortAlphabetically = { viewModel.sortAlphabetically() }
                    ),
                    enabled = !isSheetOpen
                )
            },
            floatingActionButton = {
                if (!isSheetOpen) {
                    FloatingActionButton(
                        onClick = { showAddDialog = true },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить товар")
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            ProductsContent(innerPadding, state, viewModel) { editingItem = it }
        }

        // Dimmed overlay when sheet is open
        AnimatedVisibility(
            visible = isSheetOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.32f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        val originalName = editingItem?.name ?: ""
                        val originalQty = editingItem?.quantity?.let { qty ->
                            if (qty % 1.0 == 0.0) qty.toInt().toString() else qty.toString()
                        } ?: ""
                        val originalUnit = editingItem?.unit ?: ""
                        
                        val isModified = nameInput != originalName ||
                                qtyInput != originalQty ||
                                unitInput != originalUnit

                        if (isModified && (nameInput.isNotBlank() || qtyInput.isNotBlank())) {
                            showCancelConfirmDialog = true
                        } else {
                            showAddDialog = false
                            editingItem = null
                        }
                    }
            )
        }

        // Custom sliding Bottom Sheet
        AnimatedVisibility(
            visible = isSheetOpen,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
        ) {
            val isSaveEnabled = nameInput.isNotBlank() &&
                    qtyInput.isNotBlank() &&
                    units.contains(unitInput) &&
                    qtyInput.replace(',', '.').toDoubleOrNull()?.let { it > 0.0 && it <= MAX_PRODUCT_QUANTITY } == true

            BottomSheetContent(
                name = nameInput,
                onNameChange = { nameInput = it },
                qtyStr = qtyInput,
                onQtyChange = { input ->
                    val decimalRegex = Regex("^\\d*[.,]?\\d*$")
                    if (input.isEmpty() || input.matches(decimalRegex)) {
                        val num = input.replace(',', '.').toDoubleOrNull()
                        if (num == null || num <= MAX_PRODUCT_QUANTITY) {
                            qtyInput = input
                        }
                    }
                },
                unit = unitInput,
                onUnitChange = { unitInput = it },
                suggestions = suggestions,
                onQueryChange = { viewModel.updateSuggestionQuery(it) },
                onSaveClick = {
                    if (isSaveEnabled) {
                        val quantityDouble = qtyInput.replace(',', '.').toDoubleOrNull() ?: 1.0
                        editingItem?.let { item ->
                            viewModel.updateProduct(item, nameInput, quantityDouble, unitInput)
                        } ?: run {
                            viewModel.addProduct(nameInput, quantityDouble, unitInput)
                        }
                        showAddDialog = false
                        editingItem = null
                    }
                },
                isSaveEnabled = isSaveEnabled
            )
        }
    }

    RenameDialogWrapper(showRenameDialog, state.list?.name ?: "Продукты", viewModel) { showRenameDialog = false }
    DeleteConfirmDialogWrapper(showDeleteConfirmDialog, viewModel) { showDeleteConfirmDialog = false }
    CancelConfirmDialogWrapper(
        visible = showCancelConfirmDialog,
        onDismiss = { showCancelConfirmDialog = false },
        onConfirm = {
            showAddDialog = false
            editingItem = null
        }
    )
}

@Composable
fun ProductsContent(
    innerPadding: PaddingValues,
    state: ProductsUiState,
    viewModel: ProductsViewModel,
    onEdit: (ShoppingItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        if (state.items.isEmpty()) {
            EmptyState()
        } else {
            ProductList(
                items = state.items,
                onToggleBought = { viewModel.toggleProductBought(it) },
                onDelete = { viewModel.deleteProduct(it) },
                onEdit = onEdit,
                onMove = { from, to -> viewModel.moveItem(from, to) }
            )
        }
    }
}

@Composable
fun RenameDialogWrapper(
    visible: Boolean,
    currentName: String,
    viewModel: ProductsViewModel,
    onDismiss: () -> Unit
) {
    if (visible) {
        RenameListDialog(
            currentName = currentName,
            onDismiss = onDismiss,
            onSave = {
                viewModel.renameList(it)
                onDismiss()
            }
        )
    }
}

@Composable
fun DeleteConfirmDialogWrapper(
    visible: Boolean,
    viewModel: ProductsViewModel,
    onDismiss: () -> Unit
) {
    if (visible) {
        DeleteConfirmDialog(
            onDismiss = onDismiss,
            onConfirm = {
                viewModel.deleteList()
                onDismiss()
            }
        )
    }
}

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
    val moreVertBaseColor = if (MaterialTheme.isDarkTheme) Color(COLOR_DARK_MORE_VERT) else Color(COLOR_LIGHT_MORE_VERT)
    val moreVertTintColor = if (enabled) {
        moreVertBaseColor
    } else {
        moreVertBaseColor.copy(alpha = 0.38f)
    }
    TopAppBar(
        title = { Text(title, color = tintColor, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            IconButton(onClick = onBack, enabled = enabled) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = tintColor)
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }, enabled = enabled) {
                Icon(Icons.Default.MoreVert, contentDescription = "Меню", tint = moreVertTintColor)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colors.addListDialogSurface)
            ) {
                DropdownMenuItem(text = { Text("Переименовать список", color = MaterialTheme.colorScheme.onSurface) }, onClick = { actions.onRename(); expanded = false })
                DropdownMenuItem(text = { Text("Сортировать по алфавиту", color = MaterialTheme.colorScheme.onSurface) }, onClick = { actions.onSortAlphabetically(); expanded = false })
                DropdownMenuItem(text = { Text("Очистить список (удалить купленные)", color = MaterialTheme.colorScheme.onSurface) }, onClick = { actions.onClearBought(); expanded = false })
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                DropdownMenuItem(text = { Text("Удалить список", color = MaterialTheme.colorScheme.error) }, onClick = { actions.onDelete(); expanded = false })
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(112.dp))
        EmptyStateIllustration()
        Spacer(modifier = Modifier.height(48.dp))
        Text("В списке покупок пока пусто", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Нажмите на кнопку + ниже,\nчтобы добавить товары", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    }
}

@Composable
fun EmptyStateIllustration() {
    Image(
        painter = painterResource(id = com.practicum.shoppinglist.R.drawable.illustration_product_list),
        contentDescription = null,
        modifier = Modifier.size(width = 324.dp, height = 300.dp)
    )
}

@Composable
fun ProductList(
    items: List<ShoppingItem>,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit,
    onMove: (Int, Int) -> Unit
) {
    val state = rememberLazyListState()
    val dragDropState = remember { DragDropState(state, onMove) }
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize().padding(vertical = Dimens.Products.listVerticalPadding).dragDropGesture(dragDropState)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            val offset by animateDpAsState(targetValue = dragDropState.getItemOffset(index).y.dp, label = "offset")
            Box(modifier = Modifier.fillMaxWidth().graphicsLayer { translationY = offset.toPx() }) {
                SwipeableProductItem(item = item, onToggleBought = onToggleBought, onDelete = onDelete, onEdit = onEdit)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableProductItem(
    item: ShoppingItem,
    onToggleBought: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onEdit: (ShoppingItem) -> Unit
) {
    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        when (state.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                onDelete(item)
            }
            SwipeToDismissBoxValue.StartToEnd -> {
                onEdit(item)
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
            SwipeToDismissBoxValue.Settled -> {}
        }
    }
    SwipeToDismissBox(
        state = state,
        backgroundContent = { SwipeBackground(state.targetValue) },
        content = { ProductItemRow(item = item, onToggleBought = onToggleBought) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(target: SwipeToDismissBoxValue) {
    val color = when (target) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colors.swipeActionBackground
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Transparent
    }
    val alignment = if (target == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
    val icon = if (target == SwipeToDismissBoxValue.StartToEnd) Icons.Default.Edit else Icons.Default.Delete
    Box(modifier = Modifier.fillMaxSize().background(color).padding(horizontal = Dimens.Products.sheetHorizontalPadding), contentAlignment = alignment) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun ProductCheckbox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkedBgColor = if (MaterialTheme.isDarkTheme) Color(COLOR_DARK_CHECKED_BG) else Color(COLOR_LIGHT_CHECKED_BG)
    val checkmarkColor = if (MaterialTheme.isDarkTheme) Color(COLOR_DARK_TICK) else Color(COLOR_LIGHT_TICK)

    Box(
        modifier = modifier
            .size(Dimens.Products.checkboxSize)
            .clickable(onClick = onCheckedChange)
            .background(
                color = if (checked) checkedBgColor else Color.Transparent,
                shape = CircleShape
            )
            .border(
                width = Dimens.Products.checkboxBorderWidth,
                color = if (checked) checkedBgColor else MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(Dimens.Products.checkboxIconSize)
            )
        }
    }
}

@Composable
fun ProductItemRow(
    item: ShoppingItem,
    onToggleBought: (ShoppingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Products.listItemVerticalPadding, horizontal = Dimens.Products.listItemHorizontalPadding),
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
                    color = if (item.isBought) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onBackground,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else null
                )
                if (item.quantity > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    val qtyStr = if (item.quantity % 1.0 == 0.0) item.quantity.toInt().toString() else item.quantity.toString()
                    Text(
                        text = "$qtyStr ${item.unit}".trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        HorizontalDivider(color = Color(COLOR_DIVIDER))
    }
}

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
                    text = "Товар",
                    modifier = Modifier
                        .background(if (MaterialTheme.isDarkTheme) Color(COLOR_DARK_LABEL_BG) else Color(COLOR_LIGHT_LABEL_BG))
                        .padding(horizontal = 4.dp)
                )
            },
            placeholder = { Text("Добавить новый товар") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
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
        // Quantity Field
        OutlinedTextField(
            value = qtyStr,
            onValueChange = onQtyChange,
            label = {
                Text(
                    text = "Количество",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .background(if (MaterialTheme.isDarkTheme) Color(COLOR_DARK_LABEL_BG) else Color(COLOR_LIGHT_LABEL_BG))
                        .padding(horizontal = 4.dp)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(Dimens.Products.fieldWidthQuantity).height(Dimens.Products.textFieldHeight),
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

        // Unit Dropdown
        var isUnitsExpanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.width(Dimens.Products.fieldWidthUnit)) {
            OutlinedTextField(
                value = unit,
                onValueChange = {
                    onUnitChange(it)
                    isUnitsExpanded = true
                },
                readOnly = false,
                label = {
                    Text(
                        text = "Единицы",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .background(if (MaterialTheme.isDarkTheme) Color(COLOR_DARK_LABEL_BG) else Color(COLOR_LIGHT_LABEL_BG))
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
                        if (focusState.isFocused) {
                            isUnitsExpanded = true
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
                    isUnitsExpanded = false
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
                            isUnitsExpanded = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }

        // Quantity adjustment buttons container - fixed width 96.dp
        Row(
            modifier = Modifier
                .width(Dimens.Products.quantityAdjustmentContainerWidth)
                .height(Dimens.Products.quantityAdjustmentContainerHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement Button
            val isDecrementEnabled = quantityDouble > 1.0
            Box(
                modifier = Modifier
                    .size(Dimens.Products.buttonClickSize)
                    .clip(CircleShape)
                    .clickable(enabled = isDecrementEnabled) {
                        val current = qtyStr.replace(',', '.').toDoubleOrNull() ?: 1.0
                        if (current > 1.0) {
                            val next = current - 1.0
                            onQtyChange(if (next % 1.0 == 0.0) next.toInt().toString() else next.toString())
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimens.Products.buttonCircleSize)
                        .background(
                            color = if (isDecrementEnabled) MaterialTheme.colors.iconPickerItemContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val minusColor = if (isDecrementEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
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

            // Increment Button
            Box(
                modifier = Modifier
                    .size(Dimens.Products.buttonClickSize)
                    .clip(CircleShape)
                    .clickable {
                        val current = qtyStr.replace(',', '.').toDoubleOrNull() ?: 0.0
                        val next = current + 1.0
                        if (next <= MAX_PRODUCT_QUANTITY) {
                            onQtyChange(if (next % 1.0 == 0.0) next.toInt().toString() else next.toString())
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
                        // Horizontal
                        drawLine(
                            color = plusColor,
                            start = Offset(0f, halfHeight),
                            end = Offset(size.width, halfHeight),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Butt
                        )
                        // Vertical
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
}

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

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.iconPickerSheetSurface,
                    shape = RoundedCornerShape(topStart = Dimens.Products.sheetCornerRadius, topEnd = Dimens.Products.sheetCornerRadius)
                )
                .border(
                    width = Dimens.Products.sheetBorderWidth,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = Dimens.Products.sheetCornerRadius, topEnd = Dimens.Products.sheetCornerRadius)
                )
                .navigationBarsPadding()
                .padding(horizontal = Dimens.Products.sheetHorizontalPadding)
                .padding(bottom = Dimens.Products.sheetBottomPadding, top = Dimens.Products.sheetTopPadding)
        ) {
            // Drag handle representation
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = Dimens.Products.dragHandleVerticalPadding)
                    .size(width = Dimens.Products.dragHandleWidth, height = Dimens.Products.dragHandleHeight)
                    .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp))
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
                contentDescription = "Сохранить",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun RenameListDialog(currentName: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var name by remember { mutableStateOf(currentName) }
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Переименовать список", color = colors.addListDialogTitle) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название списка") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.addListDialogAccent,
                    focusedLabelColor = colors.addListDialogTitle,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(name) }, enabled = name.isNotBlank()) {
                Text("Сохранить", color = colors.addListDialogAccent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = colors.addListDialogPlaceholder)
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun DeleteConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить список?", color = colors.addListDialogTitle) },
        text = { Text("Вы уверены, что хотите безвозвратно удалить этот список покупок?", color = colors.addListDialogPlaceholder) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Удалить", color = MaterialTheme.colors.confirmDialogDeleteText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = colors.addListDialogPlaceholder)
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun CancelConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val colors = MaterialTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Отменить добавление?", color = colors.addListDialogTitle) },
        text = { Text("Введенные данные будут потеряны.", color = colors.addListDialogPlaceholder) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Да", color = colors.addListDialogAccent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет", color = colors.addListDialogPlaceholder)
            }
        },
        containerColor = colors.addListDialogSurface
    )
}

@Composable
fun CancelConfirmDialogWrapper(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (visible) {
        CancelConfirmDialog(
            onDismiss = onDismiss,
            onConfirm = {
                onConfirm()
                onDismiss()
            }
        )
    }
}

@Suppress("MagicNumber")
@Preview(showBackground = true, name = "Light Theme")
@Preview(showBackground = true, name = "Dark Theme", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductsScreenPreview() {
    com.practicum.shoppinglist.presentation.theme.Theme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                val mockState = ProductsUiState(
                    list = com.practicum.shoppinglist.domain.model.ShoppingList(1L, "Продукты", "list_alt"),
                    items = listOf(
                        ShoppingItem(1L, 1L, "Молоко", 1.5, "кг", false),
                        ShoppingItem(2L, 1L, "Хлеб", 1.0, "шт", true)
                    ),
                    isLoading = false
                )
                Column {
                    ProductsTopBar(
                        title = mockState.list?.name ?: "Продукты",
                        onBack = {},
                        actions = TopBarActions(
                            onRename = {},
                            onDelete = {},
                            onClearBought = {},
                            onSortAlphabetically = {}
                        )
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        ProductList(
                            items = mockState.items,
                            onToggleBought = {},
                            onDelete = {},
                            onEdit = {},
                            onMove = { _, _ -> }
                        )
                    }
                }
            }
        }
    }
}
