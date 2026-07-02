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
import org.koin.dsl.module

val domainModule = module {
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
        CopyShoppingListUseCase(shoppingListRepository = get())
    }
    factory {
        RenameShoppingListUseCase(shoppingListRepository = get())
    }
}
