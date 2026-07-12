package com.practicum.shoppinglist.data.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.shoppinglist.data.local.database.AppDatabase
import com.practicum.shoppinglist.data.local.datasource.AuthTokenDataSource
import com.practicum.shoppinglist.data.local.datasource.ThemePreferencesDataSource
import com.practicum.shoppinglist.data.remote.auth.AuthApi
import com.practicum.shoppinglist.data.repository.AuthRepositoryImpl
import com.practicum.shoppinglist.data.repository.ProductSuggestionRepositoryImpl
import com.practicum.shoppinglist.data.repository.ShoppingItemRepositoryImpl
import com.practicum.shoppinglist.data.repository.ShoppingListRepositoryImpl
import com.practicum.shoppinglist.data.repository.ThemeRepositoryImpl
import com.practicum.shoppinglist.domain.repository.AuthRepository
import com.practicum.shoppinglist.domain.repository.ProductSuggestionRepository
import com.practicum.shoppinglist.domain.repository.ShoppingItemRepository
import com.practicum.shoppinglist.domain.repository.ShoppingListRepository
import com.practicum.shoppinglist.domain.repository.ThemeRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single {
        get<Retrofit>().create(AuthApi::class.java)
    }
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "shopping_list_database",
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                AppDatabase.seedProductSuggestions(db)
            }
        }).addMigrations(
            AppDatabase.MIGRATION_1_2,
            AppDatabase.MIGRATION_2_3,
            AppDatabase.MIGRATION_3_4,
            AppDatabase.MIGRATION_4_5,
        ).build()
    }
    single {
        get<AppDatabase>().shoppingListDao()
    }
    single {
        get<AppDatabase>().shoppingItemDao()
    }
    single {
        AuthTokenDataSource(context = androidContext())
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            authTokenDataSource = get(),
        )
    }
    single<ShoppingListRepository> {
        ShoppingListRepositoryImpl(
            shoppingListDao = get(),
            authRepository = get(),
        )
    }
    single<ShoppingItemRepository> {
        ShoppingItemRepositoryImpl(
            shoppingItemDao = get(),
            shoppingListDao = get(),
            authRepository = get()
        )
    }
    single<ProductSuggestionRepository> {
        ProductSuggestionRepositoryImpl(shoppingItemDao = get())
    }
    single {
        ThemePreferencesDataSource(context = androidContext())
    }
    single<ThemeRepository> {
        ThemeRepositoryImpl(themePreferencesDataSource = get())
    }
}

private const val AUTH_BASE_URL = "https://practicumopbackend-production.up.railway.app/"
