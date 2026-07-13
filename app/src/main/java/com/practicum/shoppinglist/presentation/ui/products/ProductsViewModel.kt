package com.practicum.shoppinglist.presentation.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.RenameShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.products.AddShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.ClearBoughtItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.CommitShoppingItemOrderUseCase
import com.practicum.shoppinglist.domain.usecase.products.DeleteAllShoppingItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.DeleteShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.GetProductSuggestionsUseCase
import com.practicum.shoppinglist.domain.usecase.products.GetShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.products.ObserveShoppingItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.ToggleShoppingItemBoughtUseCase
import com.practicum.shoppinglist.domain.usecase.products.UpdateShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.UpdateShoppingListSortTypeUseCase
import com.practicum.shoppinglist.presentation.ui.common.SortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProductsUiState(
    val list: ShoppingList? = null,
    val items: List<ShoppingItem> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val listDeleted: Boolean = false,
    val sortType: SortType = SortType.Custom
)

@Suppress("LongParameterList")
class ProductsViewModel(
    private val listId: Long,
    private val renameShoppingListUseCase: RenameShoppingListUseCase,
    private val deleteAllShoppingItemsUseCase: DeleteAllShoppingItemsUseCase,
    private val getShoppingListUseCase: GetShoppingListUseCase,
    private val observeShoppingItemsUseCase: ObserveShoppingItemsUseCase,
    private val addShoppingItemUseCase: AddShoppingItemUseCase,
    private val updateShoppingItemUseCase: UpdateShoppingItemUseCase,
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase,
    private val toggleShoppingItemBoughtUseCase: ToggleShoppingItemBoughtUseCase,
    private val clearBoughtItemsUseCase: ClearBoughtItemsUseCase,
    private val commitShoppingItemOrderUseCase: CommitShoppingItemOrderUseCase,
    private val getProductSuggestionsUseCase: GetProductSuggestionsUseCase,
    private val updateShoppingListSortTypeUseCase: UpdateShoppingListSortTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private val suggestionQuery = MutableStateFlow("")

    private var persistedItems: List<ShoppingItem> = emptyList()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val suggestions: StateFlow<List<String>> = suggestionQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                getProductSuggestionsUseCase(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val list = getShoppingListUseCase(listId)
            if (list == null) {
                _uiState.value = _uiState.value.copy(listDeleted = true, isLoading = false)
            } else {
                val sortType = runCatching { SortType.valueOf(list.sortType) }
                    .getOrDefault(SortType.Custom)
                _uiState.value = _uiState.value.copy(list = list, sortType = sortType)
                observeShoppingItemsUseCase(listId).collect { items ->
                    persistedItems = items
                    _uiState.value = _uiState.value.copy(
                        items = sortForDisplay(items, _uiState.value.sortType),
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun sortForDisplay(items: List<ShoppingItem>, sortType: SortType): List<ShoppingItem> {
        return if (sortType == SortType.Alphabetical) {
            items.sortedBy { it.name.lowercase() }
        } else {
            items
        }
    }

    fun addProduct(name: String, quantity: Double, unit: String) {
        viewModelScope.launch {
            addShoppingItemUseCase(
                listId = listId,
                name = name,
                quantity = quantity,
                unit = unit,
                currentItems = _uiState.value.items
            )
            _uiState.value = _uiState.value.copy(items = persistedItems, sortType = SortType.Custom)
        }
    }

    fun updateProduct(item: ShoppingItem, name: String, quantity: Double, unit: String) {
        viewModelScope.launch {
            updateShoppingItemUseCase(item, name, quantity, unit)
        }
    }

    fun deleteProduct(item: ShoppingItem) {
        viewModelScope.launch {
            deleteShoppingItemUseCase(item)
        }
    }

    fun toggleProductBought(item: ShoppingItem) {
        viewModelScope.launch {
            toggleShoppingItemBoughtUseCase(item)
        }
    }

    fun renameList(newName: String) {
        viewModelScope.launch {
            _uiState.value.list?.let { currentList ->
                renameShoppingListUseCase(listId, newName.trim())
                _uiState.value = _uiState.value.copy(
                    list = currentList.copy(name = newName.trim())
                )
            }
        }
    }

    fun deleteAllItems() {
        viewModelScope.launch {
            deleteAllShoppingItemsUseCase(listId)
        }
    }

    fun clearBoughtItems() {
        viewModelScope.launch {
            clearBoughtItemsUseCase(listId)
        }
    }

    fun selectSortType(sortType: SortType) {
        _uiState.value = _uiState.value.copy(
            items = sortForDisplay(persistedItems, sortType),
            sortType = sortType
        )
        viewModelScope.launch {
            updateShoppingListSortTypeUseCase(listId, sortType.name)
        }
    }

    fun reorderItems(fromIndex: Int, toIndex: Int) {
        val currentItems = _uiState.value.items.toMutableList()
        if (fromIndex in currentItems.indices && toIndex in currentItems.indices) {
            val item = currentItems.removeAt(fromIndex)
            currentItems.add(toIndex, item)
            val updated = currentItems.mapIndexed { index, shoppingItem -> shoppingItem.copy(sortOrder = index) }
            persistedItems = updated
            _uiState.value = _uiState.value.copy(items = updated, sortType = SortType.Custom)
        }
    }

    fun commitItemOrder() {
        viewModelScope.launch {
            commitShoppingItemOrderUseCase(_uiState.value.items)
            updateShoppingListSortTypeUseCase(listId, SortType.Custom.name)
        }
    }

    fun updateSuggestionQuery(query: String) {
        suggestionQuery.value = query
    }
}
