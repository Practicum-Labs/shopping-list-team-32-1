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
import com.practicum.shoppinglist.domain.usecase.products.AddProductSuggestionUseCase
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
    factory {
        GetShoppingListUseCase(shoppingListRepository = get())
    }
    factory {
        ObserveShoppingItemsUseCase(shoppingItemRepository = get())
    }
    factory {
        AddShoppingItemUseCase(shoppingItemRepository = get())
    }
    factory {
        UpdateShoppingItemUseCase(shoppingItemRepository = get())
    }
    factory {
        DeleteShoppingItemUseCase(shoppingItemRepository = get())
    }
    factory {
        ToggleShoppingItemBoughtUseCase(shoppingItemRepository = get())
    }
    factory {
        ClearBoughtItemsUseCase(shoppingItemRepository = get())
    }
    factory {
        SortShoppingItemsAlphabeticallyUseCase(shoppingItemRepository = get())
    }
    factory {
        MoveShoppingItemUseCase(shoppingItemRepository = get())
    }
    factory {
        GetProductSuggestionsUseCase(shoppingItemRepository = get())
    }
    factory {
        AddProductSuggestionUseCase(shoppingItemRepository = get())
    }
}
