package com.practicum.shoppinglist

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.android.gms.security.ProviderInstaller
import com.practicum.shoppinglist.data.di.dataModule
import com.practicum.shoppinglist.data.remote.network.TlsProviderGate
import com.practicum.shoppinglist.domain.di.domainModule
import com.practicum.shoppinglist.presentation.di.themeModule
import com.practicum.shoppinglist.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    private val tlsProviderGate: TlsProviderGate by inject()

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

        updateSecurityProvider()
    }

    private fun updateSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(
            this,
            object : ProviderInstaller.ProviderInstallListener {
                override fun onProviderInstalled() {
                    tlsProviderGate.signalReady()
                }

                override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
                    Log.w(TAG, "TLS-провайдер не обновлён (errorCode=$errorCode)")
                    tlsProviderGate.signalReady()
                }
            },
        )
    }

    private companion object {
        const val TAG = "App"
    }
}
