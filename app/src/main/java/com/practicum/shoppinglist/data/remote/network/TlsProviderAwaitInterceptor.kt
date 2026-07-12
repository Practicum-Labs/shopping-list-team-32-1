package com.practicum.shoppinglist.data.remote.network

import okhttp3.Interceptor
import okhttp3.Response

class TlsProviderAwaitInterceptor(
    private val tlsProviderGate: TlsProviderGate,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        tlsProviderGate.awaitReady()
        return chain.proceed(chain.request())
    }
}
