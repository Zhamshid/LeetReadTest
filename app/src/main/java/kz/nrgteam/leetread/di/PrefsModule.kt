package kz.nrgteam.leetread.di

import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.data.prefs.PrefsImpl
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("ABM_CO_SHARED_PREF", Context.MODE_PRIVATE)
    }

    @Provides
    fun providePrefs(sharedPreferences: SharedPreferences): Prefs {
        return PrefsImpl(sharedPreferences)
    }
}