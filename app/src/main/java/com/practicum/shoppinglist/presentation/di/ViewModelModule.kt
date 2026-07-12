package com.practicum.shoppinglist.presentation.di

import com.practicum.shoppinglist.presentation.ui.auth.login.LoginViewModel
import com.practicum.shoppinglist.presentation.ui.auth.recovery.RecoveryViewModel
import com.practicum.shoppinglist.presentation.ui.auth.register.RegisterViewModel
import com.practicum.shoppinglist.presentation.ui.main.MainViewModel
import com.practicum.shoppinglist.presentation.ui.onboarding.OnboardingViewModel
import com.practicum.shoppinglist.presentation.ui.products.ProductsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        OnboardingViewModel(observeAuthSessionUseCase = get())
    }
    viewModel {
        LoginViewModel(loginUseCase = get())
    }
    viewModel {
        RegisterViewModel(registerUseCase = get())
    }
    viewModel {
        RecoveryViewModel(recoverPasswordUseCase = get())
    }
    viewModel {
        MainViewModel(
            observeShoppingListsUseCase = get(),
            createShoppingListUseCase = get(),
            updateShoppingListIconUseCase = get(),
            deleteAllShoppingListsUseCase = get(),
            deleteShoppingListUseCase = get(),
            copyShoppingListUseCase = get(),
            renameShoppingListUseCase = get(),
            checkAuthUseCase = get(),
            logoutUseCase = get(),
        )
    }
    viewModel { (listId: Long) ->
        ProductsViewModel(
            listId = listId,
            renameShoppingListUseCase = get(),
            deleteAllShoppingItemsUseCase = get(),
            getShoppingListUseCase = get(),
            observeShoppingItemsUseCase = get(),
            addShoppingItemUseCase = get(),
            updateShoppingItemUseCase = get(),
            deleteShoppingItemUseCase = get(),
            toggleShoppingItemBoughtUseCase = get(),
            clearBoughtItemsUseCase = get(),
            commitShoppingItemOrderUseCase = get(),
            getProductSuggestionsUseCase = get(),
            updateShoppingListSortTypeUseCase = get(),
        )
    }
}
