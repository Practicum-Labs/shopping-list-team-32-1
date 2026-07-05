package com.practicum.shoppinglist.presentation.ui.products

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.ui.main.SortType
import com.practicum.shoppinglist.presentation.ui.main.components.ConfirmationDialog
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListMenuBottomSheet
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListMenuContent
import com.practicum.shoppinglist.presentation.ui.main.components.SwipeableListItem
import com.practicum.shoppinglist.presentation.ui.main.shoppingListIconByName
import kotlinx.coroutines.launch

val CreamBackground = Color(0xFFFFFBF7)
val SandAccent = Color(0xFFFFD8BE)
val TextDark = Color(0xFF221C18)
val TextSecondary = Color(0xFF635B55)
val CircleBackdrop = Color(0xFFDECBBF)
val CartPeach = Color(0xFFF3C08D)

data class ProductsScreenActions(
    val onToggleBought: (ShoppingItemEntity) -> Unit = {},
    val onDeleteProduct: (ShoppingItemEntity) -> Unit = {},
    val onReorder: (Int, Int) -> Unit = { _, _ -> },
    val onCommitOrder: () -> Unit = {},
    val onClearBought: () -> Unit = {},
    val onClearAllItems: () -> Unit = {},
    val onSortAlphabetically: () -> Unit = {},
    val onUpdateSuggestionQuery: (String) -> Unit = {},
    val onAddProduct: (name: String, quantity: Double, unit: String) -> Unit = { _, _, _ -> },
    val onUpdateProduct: (item: ShoppingItemEntity, name: String, quantity: Double, unit: String) -> Unit = { _, _, _, _ -> }
)

@Composable
fun ProductsScreen(viewModel: ProductsViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    ProductsScreenContent(
        state = state,
        suggestions = suggestions,
        onBack = onBack,
        actions = ProductsScreenActions(
            onToggleBought = { viewModel.toggleProductBought(it) },
            onDeleteProduct = { viewModel.deleteProduct(it) },
            onReorder = { from, to -> viewModel.reorderItems(from, to) },
            onCommitOrder = { viewModel.commitItemOrder() },
            onClearBought = { viewModel.clearBought() },
            onClearAllItems = { viewModel.clearAllItems() },
            onSortAlphabetically = { viewModel.sortAlphabetically() },
            onUpdateSuggestionQuery = { viewModel.updateSuggestionQuery(it) },
            onAddProduct = { name, quantity, unit -> viewModel.addProduct(name, quantity, unit) },
            onUpdateProduct = { item, name, quantity, unit -> viewModel.updateProduct(item, name, quantity, unit) }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreenContent(
    state: ProductsUiState,
    suggestions: List<String>,
    onBack: () -> Unit,
    actions: ProductsScreenActions = ProductsScreenActions()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ShoppingItemEntity?>(null) }
    var showMenuSheet by remember { mutableStateOf(false) }
    var pendingMenuAction by remember { mutableStateOf<PendingMenuAction?>(null) }
    val menuSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()

    fun closeMenuSheet() {
        coroutineScope.launch { menuSheetState.hide() }.invokeOnCompletion { showMenuSheet = false }
    }

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

    val isSheetOpen = showAddDialog || editingItem != null

    fun closeSheet() {
        val currentEditingItem = editingItem
        if (currentEditingItem != null && nameInput.isNotBlank()) {
            val quantityDouble = qtyInput.toDoubleOrNull() ?: 1.0
            actions.onUpdateProduct(currentEditingItem, nameInput, quantityDouble, unitInput)
        }
        coroutineScope.launch { editSheetState.hide() }.invokeOnCompletion {
            showAddDialog = false
            editingItem = null
        }
    }

    Scaffold(
        topBar = {
            ProductsTopBar(
                title = state.list?.name ?: "Продукты",
                onBack = onBack,
                onMenuClick = { showMenuSheet = true }
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
            ProductsContent(
                innerPadding = innerPadding,
                state = state,
                onToggleBought = actions.onToggleBought,
                onDeleteProduct = actions.onDeleteProduct,
                onReorder = actions.onReorder,
                onCommitOrder = actions.onCommitOrder,
                onEdit = { editingItem = it }
            )
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { closeSheet() },
            sheetState = editSheetState,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colors.iconPickerSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            BottomSheetContent(
                modifier = Modifier.fillMaxHeight(),
                autoFocusName = editingItem == null,
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
                onQueryChange = actions.onUpdateSuggestionQuery,
                onSaveClick = {
                    if (nameInput.isNotBlank()) {
                        val quantityDouble = qtyInput.toDoubleOrNull() ?: 1.0
                        if (editingItem != null) {
                            actions.onUpdateProduct(editingItem!!, nameInput, quantityDouble, unitInput)
                        } else {
                            actions.onAddProduct(nameInput, quantityDouble, unitInput)
                        }
                        coroutineScope.launch { editSheetState.hide() }.invokeOnCompletion {
                            showAddDialog = false
                            editingItem = null
                        }
                    }
                }
            )
        }
    }

    if (showMenuSheet) {
        ShoppingListMenuBottomSheet(
            sheetState = menuSheetState,
            currentSortType = state.sortType,
            onDismissRequest = { showMenuSheet = false },
            onSortTypeSelected = { sortType ->
                if (sortType == SortType.Alphabetical) {
                    actions.onSortAlphabetically()
                }
                closeMenuSheet()
            },
            onDeleteAllClick = {
                pendingMenuAction = PendingMenuAction.DeleteAll
                closeMenuSheet()
            },
            onClearPurchasedClick = {
                pendingMenuAction = PendingMenuAction.ClearBought
                closeMenuSheet()
            }
        )
    }

    when (pendingMenuAction) {
        PendingMenuAction.ClearBought -> ConfirmationDialog(
            title = "Удалить все купленные товары?",
            confirmText = "Удалить",
            cancelText = "Отмена",
            onConfirm = {
                actions.onClearBought()
                pendingMenuAction = null
            },
            onDismiss = { pendingMenuAction = null }
        )
        PendingMenuAction.DeleteAll -> ConfirmationDialog(
            title = "Удалить все товары?",
            confirmText = "Удалить",
            cancelText = "Отмена",
            onConfirm = {
                actions.onClearAllItems()
                pendingMenuAction = null
            },
            onDismiss = { pendingMenuAction = null }
        )
        null -> Unit
    }
}

private enum class PendingMenuAction {
    ClearBought,
    DeleteAll
}

@Composable
fun ProductsContent(
    innerPadding: PaddingValues,
    state: ProductsUiState,
    onToggleBought: (ShoppingItemEntity) -> Unit,
    onDeleteProduct: (ShoppingItemEntity) -> Unit,
    onReorder: (Int, Int) -> Unit,
    onCommitOrder: () -> Unit,
    onEdit: (ShoppingItemEntity) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        if (state.items.isEmpty()) {
            EmptyState()
        } else {
            ProductList(
                items = state.items,
                onToggleBought = onToggleBought,
                onDelete = onDeleteProduct,
                onEdit = onEdit,
                onMove = onReorder,
                onDragEnd = onCommitOrder
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsTopBar(
    title: String,
    onBack: () -> Unit,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.onBackground)
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.MoreVert, contentDescription = "Меню", tint = MaterialTheme.colorScheme.onBackground)
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
    onDragEnd: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val state = rememberLazyListState()
    val dragDropState = remember { DragDropState(state, onMove, onDragEnd) }
    val draggedIndex = dragDropState.draggedIndex

    Box(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
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

@Composable
fun SwipeableProductItem(
    item: ShoppingItemEntity,
    onToggleBought: (ShoppingItemEntity) -> Unit,
    onDelete: (ShoppingItemEntity) -> Unit,
    onEdit: (ShoppingItemEntity) -> Unit,
    isDragging: Boolean = false
) {
    SwipeableListItem(
        onDelete = { onDelete(item) },
        actionsWidth = Dimens.Main.swipeActionsWidthTwoButtons,
        backgroundShape = RectangleShape,
        backgroundContent = { isLongSwipe, closeItem ->
            ProductSwipeBackground(
                isLongSwipe = isLongSwipe,
                onEditClick = { closeItem(); onEdit(item) },
                onDeleteClick = { closeItem(); onDelete(item) }
            )
        },
        content = { ProductItemRow(item = item, onToggleBought = onToggleBought, isDragging = isDragging) }
    )
}

@Composable
fun ProductSwipeBackground(
    isLongSwipe: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = Dimens.Main.swipeActionContainerStartPadding, end = 0.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (isLongSwipe) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(Dimens.Main.swipeActionButtonSize),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Удалить",
                            modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                        )
                    }
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(Dimens.Main.swipeActionButtonSize)
                        .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Редактировать",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                    )
                }
                Spacer(modifier = Modifier.width(Dimens.Main.swipeActionSpacing))
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(Dimens.Main.swipeActionButtonSize)
                        .background(MaterialTheme.colors.swipeActionBackground, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.Main.swipeActionIconSize)
                    )
                }
            }
        }
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
    modifier: Modifier = Modifier,
    autoFocusName: Boolean = false
) {
    val quantityDouble = qtyStr.toDoubleOrNull() ?: 1.0
    val focusManager = LocalFocusManager.current
    val unitFocusRequester = remember { FocusRequester() }
    val nameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (autoFocusName) {
            nameFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier
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
            .imePadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp, top = 8.dp)
    ) {
        // Drag handle + Save button row
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 8.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp))
            )

            FloatingActionButton(
                onClick = onSaveClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Сохранить",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocusRequester),
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
    }

private val productListPreviewItems = listOf(
    ShoppingItemEntity(id = 1, listId = 1, name = "Молоко", quantity = 1.0, unit = "л.", isBought = false, sortOrder = 0),
    ShoppingItemEntity(id = 2, listId = 1, name = "Хлеб", quantity = 2.0, unit = "шт.", isBought = false, sortOrder = 1),
    ShoppingItemEntity(id = 3, listId = 1, name = "Яблоки", quantity = 1.5, unit = "кг.", isBought = true, sortOrder = 2),
    ShoppingItemEntity(id = 4, listId = 1, name = "Сыр", quantity = 300.0, unit = "г.", isBought = false, sortOrder = 3),
    ShoppingItemEntity(id = 5, listId = 1, name = "Кофе", quantity = 1.0, unit = "шт.", isBought = false, sortOrder = 4),
)

private val productsScreenPreviewState = ProductsUiState(
    list = ShoppingList(id = 1, name = "Выходные", iconName = "shopping_bag"),
    items = productListPreviewItems,
    isLoading = false
)

@Preview(
    name = "Screen Light",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun ProductsScreenLightPreview() {
    Theme {
        ProductsScreenContent(
            state = productsScreenPreviewState,
            suggestions = emptyList(),
            onBack = {}
        )
    }
}

@Preview(
    name = "Screen Dark",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ProductsScreenDarkPreview() {
    Theme(darkTheme = true) {
        ProductsScreenContent(
            state = productsScreenPreviewState,
            suggestions = emptyList(),
            onBack = {}
        )
    }
}

@Preview(
    name = "Screen Empty",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
)
@Composable
private fun ProductsScreenEmptyPreview() {
    Theme {
        ProductsScreenContent(
            state = ProductsUiState(
                list = ShoppingList(id = 1, name = "Выходные", iconName = "shopping_bag"),
                items = emptyList(),
                isLoading = false
            ),
            suggestions = emptyList(),
            onBack = {}
        )
    }
}

@Composable
private fun ProductsScreenWithMenuSheetPreviewContent(
    currentSortType: SortType,
    initialSortExpanded: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ProductsScreenContent(
            state = productsScreenPreviewState.copy(sortType = currentSortType),
            suggestions = emptyList(),
            onBack = {}
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colors.menuSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(
                topStart = Dimens.Main.menuSheetCornerRadius,
                topEnd = Dimens.Main.menuSheetCornerRadius
            )
        ) {
            ShoppingListMenuContent(
                currentSortType = currentSortType,
                onSortTypeSelected = {},
                onDeleteAllClick = {},
                onClearPurchasedClick = {},
                initialSortExpanded = initialSortExpanded
            )
        }
    }
}

@Preview(name = "Screen + Menu Sheet - Base", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun ProductsScreenWithMenuSheetBasePreview() {
    Theme {
        ProductsScreenWithMenuSheetPreviewContent(
            currentSortType = SortType.Custom,
            initialSortExpanded = false
        )
    }
}

@Preview(
    name = "Screen + Menu Sheet - Base (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductsScreenWithMenuSheetBaseDarkPreview() {
    Theme(darkTheme = true) {
        ProductsScreenWithMenuSheetPreviewContent(
            currentSortType = SortType.Custom,
            initialSortExpanded = false
        )
    }
}

@Preview(
    name = "Screen + Menu Sheet - Sort Open (Alphabetical)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908
)
@Composable
private fun ProductsScreenWithMenuSheetSortAlphabeticalPreview() {
    Theme {
        ProductsScreenWithMenuSheetPreviewContent(
            currentSortType = SortType.Alphabetical,
            initialSortExpanded = true
        )
    }
}

@Preview(
    name = "Screen + Menu Sheet - Sort Open (Alphabetical) (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductsScreenWithMenuSheetSortAlphabeticalDarkPreview() {
    Theme(darkTheme = true) {
        ProductsScreenWithMenuSheetPreviewContent(
            currentSortType = SortType.Alphabetical,
            initialSortExpanded = true
        )
    }
}

@Preview(
    name = "Screen + Menu Sheet - Sort Open (Custom)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908
)
@Composable
private fun ProductsScreenWithMenuSheetSortCustomPreview() {
    Theme {
        ProductsScreenWithMenuSheetPreviewContent(
            currentSortType = SortType.Custom,
            initialSortExpanded = true
        )
    }
}

@Preview(
    name = "Screen + Menu Sheet - Sort Open (Custom) (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductsScreenWithMenuSheetSortCustomDarkPreview() {
    Theme(darkTheme = true) {
        ProductsScreenWithMenuSheetPreviewContent(
            currentSortType = SortType.Custom,
            initialSortExpanded = true
        )
    }
}

private val previewEditingItem = ShoppingItemEntity(
    id = 10,
    listId = 1,
    name = "Йогурт",
    quantity = 4.0,
    unit = "шт.",
    isBought = false,
    sortOrder = 0
)

@Composable
private fun ProductBottomSheetPreviewContent(
    isEditing: Boolean,
    isFullScreen: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ProductsScreenContent(
            state = productsScreenPreviewState,
            suggestions = emptyList(),
            onBack = {}
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .then(if (isFullScreen) Modifier.fillMaxHeight() else Modifier),
            color = MaterialTheme.colors.iconPickerSheetSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            BottomSheetContent(
                modifier = if (isFullScreen) Modifier.fillMaxHeight() else Modifier,
                autoFocusName = false,
                name = if (isEditing) previewEditingItem.name else "",
                onNameChange = {},
                qtyStr = if (isEditing) previewEditingItem.quantity.toInt().toString() else "",
                onQtyChange = {},
                unit = if (isEditing) previewEditingItem.unit else "",
                onUnitChange = {},
                suggestions = emptyList(),
                onQueryChange = {},
                onSaveClick = {}
            )
        }
    }
}

@Preview(name = "Add Product - Compact", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun AddProductCompactPreview() {
    Theme {
        ProductBottomSheetPreviewContent(isEditing = false, isFullScreen = false)
    }
}

@Preview(
    name = "Add Product - Compact (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AddProductCompactDarkPreview() {
    Theme(darkTheme = true) {
        ProductBottomSheetPreviewContent(isEditing = false, isFullScreen = false)
    }
}

@Preview(name = "Add Product - Fullscreen", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun AddProductFullscreenPreview() {
    Theme {
        ProductBottomSheetPreviewContent(isEditing = false, isFullScreen = true)
    }
}

@Preview(
    name = "Add Product - Fullscreen (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AddProductFullscreenDarkPreview() {
    Theme(darkTheme = true) {
        ProductBottomSheetPreviewContent(isEditing = false, isFullScreen = true)
    }
}

@Preview(name = "Edit Product - Compact", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun EditProductCompactPreview() {
    Theme {
        ProductBottomSheetPreviewContent(isEditing = true, isFullScreen = false)
    }
}

@Preview(
    name = "Edit Product - Compact (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EditProductCompactDarkPreview() {
    Theme(darkTheme = true) {
        ProductBottomSheetPreviewContent(isEditing = true, isFullScreen = false)
    }
}

@Preview(name = "Edit Product - Fullscreen", showBackground = true, widthDp = 428, heightDp = 908)
@Composable
private fun EditProductFullscreenPreview() {
    Theme {
        ProductBottomSheetPreviewContent(isEditing = true, isFullScreen = true)
    }
}

@Preview(
    name = "Edit Product - Fullscreen (Dark)",
    showBackground = true,
    widthDp = 428,
    heightDp = 908,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EditProductFullscreenDarkPreview() {
    Theme(darkTheme = true) {
        ProductBottomSheetPreviewContent(isEditing = true, isFullScreen = true)
    }
}
