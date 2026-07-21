package com.practicum.shoppinglist.presentation.di

import com.practicum.shoppinglist.presentation.theme.ThemeController
import org.koin.dsl.module

val themeModule = module {
    single {
        ThemeController(
            observeDarkThemeUseCase = get(),
            setDarkThemeUseCase = get(),
        )
    }
}
