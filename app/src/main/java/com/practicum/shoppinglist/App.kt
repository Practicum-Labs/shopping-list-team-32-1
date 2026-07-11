package com.practicum.shoppinglist

import android.app.Application
import android.util.Log
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
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

        updateSecurityProvider()

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

    private fun updateSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            Log.w(TAG, "Google Play Services требует обновления для установки TLS-провайдера", e)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.w(TAG, "Google Play Services недоступны, TLS-провайдер не обновлён", e)
        }
    }

    private companion object {
        const val TAG = "App"
    }
}
