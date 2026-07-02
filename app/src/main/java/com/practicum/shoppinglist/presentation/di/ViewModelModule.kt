package com.practicum.shoppinglist.presentation.di

import com.practicum.shoppinglist.presentation.ui.main.MainViewModel
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
}
