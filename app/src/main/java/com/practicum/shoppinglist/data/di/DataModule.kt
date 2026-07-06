package com.practicum.shoppinglist.data.di

import androidx.room.Room
import com.practicum.shoppinglist.data.local.database.AppDatabase
import com.practicum.shoppinglist.data.local.datasource.ThemePreferencesDataSource
import com.practicum.shoppinglist.data.repository.ShoppingListRepositoryImpl
import com.practicum.shoppinglist.data.repository.ThemeRepositoryImpl
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import com.practicum.shoppinglist.domain.repository.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.repository.ShoppingItemRepositoryImpl
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository

val dataModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "shopping_list_database",
        ).addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }
    single {
        get<AppDatabase>().shoppingListDao()
    }
    single {
        get<AppDatabase>().shoppingItemDao()
    }
    single<ShoppingListRepository> {
        ShoppingListRepositoryImpl(shoppingListDao = get())
    }
    single<ShoppingItemRepository> {
        ShoppingItemRepositoryImpl(shoppingItemDao = get())
    }
    single {
        ThemePreferencesDataSource(context = androidContext())
    }
    single<ThemeRepository> {
        ThemeRepositoryImpl(themePreferencesDataSource = get())
    }
}
