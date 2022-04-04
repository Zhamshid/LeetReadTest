package kz.nrgteam.leetread.data.cloud

import kz.nrgteam.leetread.data.prefs.Prefs
import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(
    val context: Context,
    val prefs: Prefs
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        // If token has been saved, add it to the request
        prefs.getAccessToken().let {
            Log.i("token", it)
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        val response = chain.proceed(requestBuilder.build())
        if(response.code == 405){
            prefs.removeAccessToken()
            prefs.removeUserId()
            prefs.setBaseUrl("")
        }

        return response
    }
}