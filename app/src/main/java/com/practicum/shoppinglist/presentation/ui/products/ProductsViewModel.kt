package com.practicum.shoppinglist.presentation.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.RenameShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.DeleteShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.products.AddShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.ClearBoughtItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.DeleteShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.GetProductSuggestionsUseCase
import com.practicum.shoppinglist.domain.usecase.products.GetShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.products.MoveShoppingItemUseCase
import com.practicum.shoppinglist.domain.usecase.products.ObserveShoppingItemsUseCase
import com.practicum.shoppinglist.domain.usecase.products.SortShoppingItemsAlphabeticallyUseCase
import com.practicum.shoppinglist.domain.usecase.products.ToggleShoppingItemBoughtUseCase
import com.practicum.shoppinglist.domain.usecase.products.UpdateShoppingItemUseCase
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
    val listDeleted: Boolean = false
)

class ProductsViewModel(
    private val listId: Long,
    private val renameShoppingListUseCase: RenameShoppingListUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase,
    private val getShoppingListUseCase: GetShoppingListUseCase,
    private val observeShoppingItemsUseCase: ObserveShoppingItemsUseCase,
    private val addShoppingItemUseCase: AddShoppingItemUseCase,
    private val updateShoppingItemUseCase: UpdateShoppingItemUseCase,
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase,
    private val toggleShoppingItemBoughtUseCase: ToggleShoppingItemBoughtUseCase,
    private val clearBoughtItemsUseCase: ClearBoughtItemsUseCase,
    private val sortShoppingItemsAlphabeticallyUseCase: SortShoppingItemsAlphabeticallyUseCase,
    private val moveShoppingItemUseCase: MoveShoppingItemUseCase,
    private val getProductSuggestionsUseCase: GetProductSuggestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private val suggestionQuery = MutableStateFlow("")

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
                _uiState.value = _uiState.value.copy(list = list, isLoading = false)
                observeShoppingItemsUseCase(listId).collect { items ->
                    _uiState.value = _uiState.value.copy(items = items)
                }
            }
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
            val currentList = _uiState.value.list ?: return@launch
            renameShoppingListUseCase(listId, newName.trim())
            _uiState.value = _uiState.value.copy(list = currentList.copy(name = newName.trim()))
        }
    }

    fun deleteList() {
        viewModelScope.launch {
            deleteShoppingListUseCase(listId)
            _uiState.value = _uiState.value.copy(listDeleted = true)
        }
    }

    fun clearBought() {
        viewModelScope.launch {
            clearBoughtItemsUseCase(listId)
        }
    }

    fun sortAlphabetically() {
        viewModelScope.launch {
            sortShoppingItemsAlphabeticallyUseCase(_uiState.value.items)
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            val updated = moveShoppingItemUseCase(fromIndex, toIndex, _uiState.value.items)
            _uiState.value = _uiState.value.copy(items = updated)
        }
    }

    fun updateSuggestionQuery(query: String) {
        suggestionQuery.value = query
    }


}
