package com.practicum.shoppinglist.presentation.di

import com.practicum.shoppinglist.presentation.ui.main.MainViewModel
import com.practicum.shoppinglist.presentation.ui.products.ProductsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(
            observeShoppingListsUseCase = get(),
            createShoppingListUseCase = get(),
            updateShoppingListIconUseCase = get(),
            deleteAllShoppingListsUseCase = get(),
            deleteShoppingListUseCase = get(),
            copyShoppingListUseCase = get(),
            renameShoppingListUseCase = get(),
        )
    }
    viewModel { (listId: Long) ->
        ProductsViewModel(
            listId = listId,
            listRepository = get(),
            itemRepository = get(),
        )
    }
}
