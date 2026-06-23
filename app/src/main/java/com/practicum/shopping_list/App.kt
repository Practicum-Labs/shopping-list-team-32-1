package com.practicum.shopping_list

import android.app.Application
import com.practicum.shopping_list.data.di.dataModule
import com.practicum.shopping_list.domain.di.domainModule
import com.practicum.shopping_list.presentation.di.viewModelModule
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
                viewModelModule,
            )
        }
    }
}
