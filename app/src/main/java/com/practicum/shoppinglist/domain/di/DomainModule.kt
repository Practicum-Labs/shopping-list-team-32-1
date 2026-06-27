package com.practicum.shoppinglist.domain.di

import com.practicum.shoppinglist.domain.theme.ObserveDarkThemeUseCase
import com.practicum.shoppinglist.domain.theme.SetDarkThemeUseCase
import com.practicum.shoppinglist.domain.usecase.CreateShoppingListUseCase
import com.practicum.shoppinglist.domain.usecase.ObserveShoppingListsUseCase
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
}
