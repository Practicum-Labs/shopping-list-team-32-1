package com.practicum.shoppinglist.domain.di

import com.practicum.shoppinglist.domain.usecase.CopyShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.CreateShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.DeleteAllShoppingListsUseCase
import com.practicum.shoppinglist.domain.usecase.DeleteShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.ObserveDarkThemeUseCase
import com.practicum.shoppinglist.domain.usecase.ObserveShoppingListsUseCase
import com.practicum.shoppinglist.domain.usecase.RenameShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.SetDarkThemeUseCase
import com.practicum.shoppinglist.domain.usecase.UpdateShoppingListIconUseCase
import com.practicum.shoppinglist.domain.usecase.auth.CheckAuthUseCase
import com.practicum.shoppinglist.domain.usecase.auth.LoginUseCase
import com.practicum.shoppinglist.domain.usecase.auth.LogoutUseCase
import com.practicum.shoppinglist.domain.usecase.auth.ObserveAuthSessionUseCase
import com.practicum.shoppinglist.domain.usecase.auth.RecoverPasswordUseCase
import com.practicum.shoppinglist.domain.usecase.auth.RegisterUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        LoginUseCase(authRepository = get())
    }
    factory {
        RegisterUseCase(authRepository = get())
    }
    factory {
        RecoverPasswordUseCase(authRepository = get())
    }
    factory {
        CheckAuthUseCase(authRepository = get())
    }
    factory {
        LogoutUseCase(authRepository = get())
    }
    factory {
        ObserveAuthSessionUseCase(authRepository = get())
    }
    factory {
        ObserveShoppingListsUseCase(shoppingListRepository = get())
    }
    factory {
        CreateShoppingListUseCase(shoppingListRepository = get())
    }
    factory {
        UpdateShoppingListIconUseCase(shoppingListRepository = get())
    }
    factory {
        ObserveDarkThemeUseCase(themeRepository = get())
    }
    factory {
        SetDarkThemeUseCase(themeRepository = get())
    }
    factory {
        DeleteAllShoppingListsUseCase(shoppingListRepository = get())
    }
    factory {
        DeleteShoppingListUseCase(shoppingListRepository = get())
    }
    factory {
        CopyShoppingListUseCase(
            shoppingListRepository = get(),
            shoppingItemRepository = get(),
        )
    }
    factory {
        RenameShoppingListUseCase(shoppingListRepository = get())
    }
}
