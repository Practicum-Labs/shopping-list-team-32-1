package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import androidx.compose.ui.zIndex
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.shoppingListIconByName

val CreamBackground = Color(0xFFFFFBF7)
val SandAccent = Color(0xFFFFD8BE)
val TextDark = Color(0xFF221C18)
val TextSecondary = Color(0xFF635B55)
val CircleBackdrop = Color(0xFFDECBBF)
val CartPeach = Color(0xFFF3C08D)

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
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ShoppingItemEntity?>(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Input States
    var nameInput by remember { mutableStateOf("") }
    var qtyInput by remember { mutableStateOf("") }
    var unitInput by remember { mutableStateOf("") }

    LaunchedEffect(editingItem) {
        if (editingItem != null) {
            nameInput = editingItem!!.name
            qtyInput = editingItem!!.quantity.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() }
            unitInput = editingItem!!.unit
        } else {
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
                )
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
        Box(modifier = Modifier.fillMaxSize()) {
            ProductsContent(innerPadding, state, viewModel) { editingItem = it }

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
                            showAddDialog = false
                            editingItem = null
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
                BottomSheetContent(
                    name = nameInput,
                    onNameChange = { nameInput = it },
                    qtyStr = qtyInput,
                    onQtyChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            qtyInput = input
                        }
                    },
                    unit = unitInput,
                    onUnitChange = { unitInput = it },
                    suggestions = suggestions,
                    onQueryChange = { viewModel.updateSuggestionQuery(it) },
                    onSaveClick = {
                        if (nameInput.isNotBlank()) {
                            val quantityDouble = qtyInput.toDoubleOrNull() ?: 1.0
                            if (editingItem != null) {
                                viewModel.updateProduct(editingItem!!, nameInput, quantityDouble, unitInput)
                            } else {
                                viewModel.addProduct(nameInput, quantityDouble, unitInput)
                            }
                            showAddDialog = false
                            editingItem = null
                        }
                    }
                )
            }
        }
    }

    RenameDialogWrapper(showRenameDialog, state.list?.name ?: "Продукты", viewModel) { showRenameDialog = false }
    DeleteConfirmDialogWrapper(showDeleteConfirmDialog, viewModel) { showDeleteConfirmDialog = false }
}

@Composable
fun ProductsContent(
    innerPadding: PaddingValues,
    state: ProductsUiState,
    viewModel: ProductsViewModel,
    onEdit: (ShoppingItemEntity) -> Unit
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
                onMove = { from, to -> viewModel.reorderItems(from, to) },
                onDragEnd = { viewModel.commitItemOrder() }
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
    actions: TopBarActions
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.onBackground)
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Меню", tint = MaterialTheme.colorScheme.onBackground)
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
    items: List<ShoppingItemEntity>,
    onToggleBought: (ShoppingItemEntity) -> Unit,
    onDelete: (ShoppingItemEntity) -> Unit,
    onEdit: (ShoppingItemEntity) -> Unit,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit = {}
) {
    val state = rememberLazyListState()
    val dragDropState = remember { DragDropState(state, onMove, onDragEnd) }
    val draggedIndex = dragDropState.draggedIndex

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize().dragDropGesture(dragDropState)
        ) {
            itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                Box(modifier = Modifier.fillMaxWidth().alpha(if (index == draggedIndex) 0f else 1f)) {
                    SwipeableProductItem(
                        item = item,
                        onToggleBought = onToggleBought,
                        onDelete = onDelete,
                        onEdit = onEdit,
                        isDragging = draggedIndex != null
                    )
                }
            }
        }

        if (draggedIndex != null && draggedIndex in items.indices) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .offset { dragDropState.overlayOffset() }
            ) {
                SwipeableProductItem(
                    item = items[draggedIndex],
                    onToggleBought = onToggleBought,
                    onDelete = onDelete,
                    onEdit = onEdit
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableProductItem(
    item: ShoppingItemEntity,
    onToggleBought: (ShoppingItemEntity) -> Unit,
    onDelete: (ShoppingItemEntity) -> Unit,
    onEdit: (ShoppingItemEntity) -> Unit,
    isDragging: Boolean = false
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> { onDelete(item); true }
                SwipeToDismissBoxValue.StartToEnd -> { onEdit(item); false }
                else -> false
            }
        }
    )
    SwipeToDismissBox(
        state = state,
        backgroundContent = { SwipeBackground(state.targetValue) },
        content = { ProductItemRow(item = item, onToggleBought = onToggleBought, isDragging = isDragging) }
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
    Box(modifier = Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp), contentAlignment = alignment) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun ProductCheckbox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clickable(onClick = onCheckedChange)
            .background(
                color = if (checked) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                color = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ProductItemRow(
    item: ShoppingItemEntity,
    onToggleBought: (ShoppingItemEntity) -> Unit,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.listItemSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductCheckbox(
                checked = item.isBought,
                onCheckedChange = { onToggleBought(item) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (item.isBought) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onBackground,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else null,
                    fontWeight = FontWeight.Medium
                )
                if (item.quantity > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    val qtyStr = if (item.quantity % 1.0 == 0.0) item.quantity.toInt().toString() else item.quantity.toString()
                    Text(
                        text = "$qtyStr ${item.unit}".trim(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_trailingelement_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(if (isDragging) 1f else 0f)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
    modifier: Modifier = Modifier
) {
    val quantityDouble = qtyStr.toDoubleOrNull() ?: 1.0
    val focusManager = LocalFocusManager.current
    val unitFocusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.iconPickerSheetSurface,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp, top = 8.dp)
        ) {
            // Drag handle representation
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp))
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Product Name Field
            var isSuggestionsExpanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        onNameChange(it)
                        onQueryChange(it)
                        isSuggestionsExpanded = it.isNotEmpty()
                    },
                    label = { Text("Товар") },
                    placeholder = { Text("Добавить новый товар") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colors.addListDialogAccent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
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
                        .fillMaxWidth(0.9f)
                        .background(MaterialTheme.colors.addListDialogSurface)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Row with quantity and unit picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity Field
                OutlinedTextField(
                    value = qtyStr,
                    onValueChange = onQtyChange,
                    label = { Text("Количество", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    placeholder = { Text("Количест...") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.width(120.dp).height(64.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colors.addListDialogAccent,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedLabelColor = MaterialTheme.colors.addListDialogAccent,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colors.addListDialogAccent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                // Unit Dropdown
                var isUnitsExpanded by remember { mutableStateOf(false) }
                val units = listOf("л", "мл", "уп", "пач", "шт", "кг", "г")
                Box(modifier = Modifier.width(120.dp)) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Единицы", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        placeholder = { Text("Един...") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .focusRequester(unitFocusRequester),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colors.addListDialogAccent,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedLabelColor = MaterialTheme.colors.addListDialogAccent,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    // Transparent overlay to intercept clicks and open/focus correctly
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                isUnitsExpanded = true
                                unitFocusRequester.requestFocus()
                            }
                    )

                    DropdownMenu(
                        expanded = isUnitsExpanded,
                        onDismissRequest = {
                            isUnitsExpanded = false
                            focusManager.clearFocus()
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .background(MaterialTheme.colors.addListDialogSurface)
                    ) {
                        units.forEach { u ->
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

                // Buttons container (Frame 55) - fixed width 96.dp!
                Row(
                    modifier = Modifier
                        .width(96.dp)
                        .height(64.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decrement Button
                    val isDecrementEnabled = quantityDouble > 1.0
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(enabled = isDecrementEnabled) {
                                val current = qtyStr.toDoubleOrNull() ?: 1.0
                                if (current > 1.0) {
                                    val next = current - 1.0
                                    onQtyChange(if (next % 1.0 == 0.0) next.toInt().toString() else next.toString())
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
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
                            .size(48.dp)
                            .clickable {
                                val current = qtyStr.toDoubleOrNull() ?: 0.0
                                val next = current + 1.0
                                onQtyChange(if (next % 1.0 == 0.0) next.toInt().toString() else next.toString())
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
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

        // The FAB checkmark button floating exactly 32.dp above the sheet top edge
        FloatingActionButton(
            onClick = onSaveClick,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
                .offset(y = (-88).dp) // FAB height (56dp) + Gap (32dp) = 88dp offset!
                .size(56.dp)
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
