package kz.nrgteam.leetread.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorCodeInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)

        return response
    }
}