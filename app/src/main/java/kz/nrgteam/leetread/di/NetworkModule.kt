package kz.nrgteam.leetread.di

import kz.nrgteam.leetread.data.cloud.AuthInterceptor
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.cloud.repository.CloudRepository
import kz.nrgteam.leetread.data.cloud.rest.ApiService
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.data.prefs.Prefs.Companion.BASE_URL
import kz.nrgteam.leetread.utils.ErrorCodeInterceptor
import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Named(BASE_URL)
    @Provides
    fun providesBaseUrl(prefs: Prefs): String {
        return prefs.getBaseUrl()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
        @Named(BASE_URL) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.google.com")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        context: Context,
        prefs: Prefs
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
        val interceptor = HttpLoggingInterceptor()
        val interceptorAuth = AuthInterceptor(context, prefs)
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(interceptor)
            .addInterceptor(interceptorAuth)
            .addInterceptor(ErrorCodeInterceptor())
        return client.build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideCloudRepository(apIs: ApiService, prefs: Prefs): BaseCloudRepository {
        return CloudRepository(
            apIs, prefs
        )
    }


}