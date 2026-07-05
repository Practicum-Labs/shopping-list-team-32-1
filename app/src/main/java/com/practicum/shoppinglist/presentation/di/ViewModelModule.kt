package com.practicum.shoppinglist.presentation.di

import com.practicum.shoppinglist.presentation.ui.auth.login.LoginViewModel
import com.practicum.shoppinglist.presentation.ui.auth.recovery.RecoveryViewModel
import com.practicum.shoppinglist.presentation.ui.auth.register.RegisterViewModel
import com.practicum.shoppinglist.presentation.ui.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
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
            logoutUseCase = get(),
        )
    }
}
