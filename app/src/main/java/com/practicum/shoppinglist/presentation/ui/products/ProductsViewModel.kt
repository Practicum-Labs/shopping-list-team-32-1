package com.practicum.shoppinglist.presentation.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
    private val listRepository: ShoppingListRepository,
    private val itemRepository: ShoppingItemRepository
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
                itemRepository.getSuggestionsFlow(query).map { list -> list.map { it.name } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadData()
        seedDefaultSuggestions()
    }

    private fun loadData() {
        viewModelScope.launch {
            val list = listRepository.getShoppingListById(listId)
            if (list == null) {
                _uiState.value = _uiState.value.copy(listDeleted = true, isLoading = false)
            } else {
                _uiState.value = _uiState.value.copy(list = list, isLoading = false)
            }
        }
        viewModelScope.launch {
            itemRepository.getItemsForListFlow(listId).collect { items ->
                _uiState.value = _uiState.value.copy(items = items)
            }
        }
    }

    fun addProduct(name: String, quantity: Double, unit: String) {
        viewModelScope.launch {
            val trimmedName = name.trim()
            itemRepository.addSuggestion(trimmedName)
            val maxOrder = _uiState.value.items.maxOfOrNull { it.sortOrder } ?: 0
            val newItem = ShoppingItem(
                listId = listId,
                name = trimmedName,
                quantity = quantity,
                unit = unit,
                sortOrder = maxOrder + 1
            )
            itemRepository.insertItem(newItem)
        }
    }

    fun updateProduct(item: ShoppingItem, name: String, quantity: Double, unit: String) {
        viewModelScope.launch {
            val trimmedName = name.trim()
            itemRepository.addSuggestion(trimmedName)
            itemRepository.updateItem(item.copy(name = trimmedName, quantity = quantity, unit = unit))
        }
    }

    fun deleteProduct(item: ShoppingItem) {
        viewModelScope.launch {
            itemRepository.deleteItem(item)
        }
    }

    fun toggleProductBought(item: ShoppingItem) {
        viewModelScope.launch {
            itemRepository.updateItem(item.copy(isBought = !item.isBought))
        }
    }

    fun renameList(newName: String) {
        viewModelScope.launch {
            val currentList = _uiState.value.list ?: return@launch
            listRepository.updateShoppingListName(listId, newName.trim())
            _uiState.value = _uiState.value.copy(list = currentList.copy(name = newName.trim()))
        }
    }

    fun deleteList() {
        viewModelScope.launch {
            listRepository.deleteShoppingList(listId)
            _uiState.value = _uiState.value.copy(listDeleted = true)
        }
    }

    fun clearBought() {
        viewModelScope.launch {
            itemRepository.clearBoughtItems(listId)
        }
    }

    fun sortAlphabetically() {
        viewModelScope.launch {
            val sorted = _uiState.value.items.sortedBy { it.name.lowercase() }
            val updated = sorted.mapIndexed { index, item -> item.copy(sortOrder = index) }
            _uiState.value = _uiState.value.copy(items = updated)
            itemRepository.updateItems(updated)
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        val currentItems = _uiState.value.items.toMutableList()
        if (fromIndex in currentItems.indices && toIndex in currentItems.indices) {
            val item = currentItems.removeAt(fromIndex)
            currentItems.add(toIndex, item)
            val updated = currentItems.mapIndexed { index, it -> it.copy(sortOrder = index) }
            _uiState.value = _uiState.value.copy(items = updated)
            viewModelScope.launch {
                itemRepository.updateItems(updated)
            }
        }
    }

    fun updateSuggestionQuery(query: String) {
        suggestionQuery.value = query
    }

    private fun seedDefaultSuggestions() {
        viewModelScope.launch {
            val defaults = listOf(
                "Кокосовое молоко",
                "Молоко",
                "Соевое молоко",
                "Сухое молоко",
                "Хлеб",
                "Яблоки",
                "Бананы",
                "Яйца",
                "Сыр",
                "Масло"
            )
            defaults.forEach { suggestion ->
                itemRepository.addSuggestion(suggestion)
            }
        }
    }
}
