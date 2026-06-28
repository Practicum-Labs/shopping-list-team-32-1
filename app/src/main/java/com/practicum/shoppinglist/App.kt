package com.practicum.shoppinglist

import android.app.Application
import com.practicum.shoppinglist.data.di.dataModule
import com.practicum.shoppinglist.domain.di.domainModule
import com.practicum.shoppinglist.presentation.di.themeModule
import com.practicum.shoppinglist.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                dataModule,
                domainModule,
                themeModule,
                viewModelModule,
            )
        }
    }
}
