package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.presentation.ui.main.SortType
import com.practicum.shoppinglist.presentation.ui.main.components.ShoppingListMenuBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

const val MAX_PRODUCT_NAME_LENGTH = 64
const val MAX_PRODUCT_QUANTITY = 10000.0

@Composable
fun ProductsRoute(
    listId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = koinViewModel(parameters = { parametersOf(listId) })
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()

    ProductsScreen(
        state = state,
        suggestions = suggestions,
        onBack = onBack,
        onRenameList = viewModel::renameList,
        onDeleteList = viewModel::deleteList,
        onClearBought = viewModel::clearBoughtItems,
        onSortTypeSelected = viewModel::selectSortType,
        onAddProduct = viewModel::addProduct,
        onUpdateProduct = viewModel::updateProduct,
        onDeleteProduct = viewModel::deleteProduct,
        onToggleProductBought = viewModel::toggleProductBought,
        onReorderItem = viewModel::reorderItems,
        onCommitOrder = viewModel::commitItemOrder,
        onUpdateSuggestionQuery = viewModel::updateSuggestionQuery,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("CyclomaticComplexMethod", "CognitiveComplexMethod", "LongParameterList", "LongMethod")
@Composable
fun ProductsScreen(
    state: ProductsUiState,
    suggestions: List<String>,
    onBack: () -> Unit,
    onRenameList: (String) -> Unit,
    onDeleteList: () -> Unit,
    onClearBought: () -> Unit,
    onSortTypeSelected: (SortType) -> Unit,
    onAddProduct: (String, Double, String) -> Unit,
    onUpdateProduct: (ShoppingItem, String, Double, String) -> Unit,
    onDeleteProduct: (ShoppingItem) -> Unit,
    onToggleProductBought: (ShoppingItem) -> Unit,
    onReorderItem: (Int, Int) -> Unit,
    onCommitOrder: () -> Unit,
    onUpdateSuggestionQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val units = remember { context.resources.getStringArray(R.array.product_units).toList() }

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var editingItemId by rememberSaveable { mutableStateOf<Long?>(null) }
    var showRenameDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showClearBoughtConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showCancelConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showMenuSheet by rememberSaveable { mutableStateOf(false) }
    val menuSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    fun closeMenuSheet() {
        coroutineScope.launch { menuSheetState.hide() }.invokeOnCompletion { showMenuSheet = false }
    }

    val editingItem = editingItemId?.let { id -> state.items.find { item -> item.id == id } }
    var nameInput by rememberSaveable { mutableStateOf("") }
    var qtyInput by rememberSaveable { mutableStateOf("") }
    var unitInput by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.listDeleted) {
        if (state.listDeleted) onBack()
    }

    val isSheetOpen = showAddDialog || editingItem != null

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                ProductsTopBar(
                    title = state.list?.name ?: stringResource(R.string.products_default_title),
                    onBack = onBack,
                    onMenuClick = { showMenuSheet = true },
                    enabled = !isSheetOpen
                )
            },
            floatingActionButton = {
                if (!isSheetOpen) {
                    FloatingActionButton(
                        onClick = {
                            showAddDialog = true
                            nameInput = ""
                            qtyInput = ""
                            unitInput = ""
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(
                                R.string.products_add_item_content_description
                            )
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            ProductsContent(
                innerPadding = innerPadding,
                state = state,
                onToggleBought = onToggleProductBought,
                onDelete = onDeleteProduct,
                onEdit = { item ->
                    editingItemId = item.id
                    nameInput = item.name
                    qtyInput = item.quantity.let { qty ->
                        if (qty % 1.0 == 0.0) qty.toInt().toString() else qty.toString()
                    }
                    unitInput = item.unit
                },
                onMove = onReorderItem,
                onDragEnd = onCommitOrder
            )
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
                        val isModified = isItemModified(
                            nameInput = nameInput,
                            qtyInput = qtyInput,
                            unitInput = unitInput,
                            editingItem = editingItem
                        )

                        if (isModified && (nameInput.isNotBlank() || qtyInput.isNotBlank())) {
                            showCancelConfirmDialog = true
                        } else {
                            showAddDialog = false
                            editingItemId = null
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
            val isSaveEnabled = isSaveButtonEnabled(
                nameInput = nameInput,
                qtyInput = qtyInput,
                unitInput = unitInput,
                units = units
            )

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
                onQueryChange = onUpdateSuggestionQuery,
                onSaveClick = {
                    if (isSaveEnabled) {
                        val quantityDouble = qtyInput.replace(',', '.').toDoubleOrNull() ?: 1.0
                        editingItem?.let { item ->
                            onUpdateProduct(item, nameInput, quantityDouble, unitInput)
                        } ?: run {
                            onAddProduct(nameInput, quantityDouble, unitInput)
                        }
                        showAddDialog = false
                        editingItemId = null
                    }
                },
                isSaveEnabled = isSaveEnabled
            )
        }
    }

    if (showMenuSheet) {
        ShoppingListMenuBottomSheet(
            sheetState = menuSheetState,
            onDismissRequest = { showMenuSheet = false },
            currentSortType = state.sortType,
            onSortTypeSelected = onSortTypeSelected,
            onDeleteAllClick = {
                showDeleteConfirmDialog = true
                closeMenuSheet()
            },
            onClearPurchasedClick = {
                showClearBoughtConfirmDialog = true
                closeMenuSheet()
            },
            onRenameClick = {
                showRenameDialog = true
                closeMenuSheet()
            }
        )
    }

    RenameDialogWrapper(
        visible = showRenameDialog,
        currentName = state.list?.name ?: stringResource(R.string.products_default_title),
        onRename = onRenameList,
        onDismiss = { showRenameDialog = false }
    )
    DeleteConfirmDialogWrapper(
        visible = showDeleteConfirmDialog,
        onDeleteConfirm = onDeleteList,
        onDismiss = { showDeleteConfirmDialog = false }
    )
    ClearBoughtConfirmDialogWrapper(
        visible = showClearBoughtConfirmDialog,
        onClearConfirm = onClearBought,
        onDismiss = { showClearBoughtConfirmDialog = false }
    )
    CancelConfirmDialogWrapper(
        visible = showCancelConfirmDialog,
        onDismiss = { showCancelConfirmDialog = false },
        onConfirm = {
            showAddDialog = false
            editingItemId = null
        },
        isEditing = editingItem != null
    )
}

private fun isItemModified(
    nameInput: String,
    qtyInput: String,
    unitInput: String,
    editingItem: ShoppingItem?
): Boolean {
    val originalName = editingItem?.name ?: ""
    val originalQty = editingItem?.quantity?.let { qty ->
        if (qty % 1.0 == 0.0) qty.toInt().toString() else qty.toString()
    } ?: ""
    val originalUnit = editingItem?.unit ?: ""
    return nameInput != originalName || qtyInput != originalQty || unitInput != originalUnit
}

private fun isSaveButtonEnabled(
    nameInput: String,
    qtyInput: String,
    unitInput: String,
    units: List<String>
): Boolean {
    val quantity = qtyInput.replace(',', '.').toDoubleOrNull()
    return nameInput.isNotBlank() &&
        qtyInput.isNotBlank() &&
        units.contains(unitInput) &&
        quantity != null && quantity > 0.0 && quantity <= MAX_PRODUCT_QUANTITY
}
