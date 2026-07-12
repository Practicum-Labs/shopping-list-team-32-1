package com.practicum.shoppinglist.data.remote.network

import android.util.Log
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

// initiallyReady нужен тестам: без него любой запрос через Koin-граф без App.onCreate() ждал бы полный таймаут.
class TlsProviderGate(initiallyReady: Boolean = false) {
    private val latch = CountDownLatch(1).apply {
        if (initiallyReady) countDown()
    }

    fun signalReady() {
        latch.countDown()
    }

    fun awaitReady() {
        val becameReady = latch.await(AWAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        if (!becameReady) {
            Log.w(
                TAG,
                "TLS-провайдер не подтвердил готовность за ${AWAIT_TIMEOUT_MS}мс, " +
                    "запрос выполняется без подтверждения",
            )
        }
    }

    private companion object {
        const val TAG = "TlsProviderGate"
        const val AWAIT_TIMEOUT_MS = 3_000L
    }
}
