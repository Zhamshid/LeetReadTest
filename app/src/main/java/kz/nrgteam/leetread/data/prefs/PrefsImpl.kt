package kz.nrgteam.leetread.data.prefs

import kz.nrgteam.leetread.data.prefs.Prefs.Companion.ACCESS_TOKEN
import kz.nrgteam.leetread.data.prefs.Prefs.Companion.BASE_URL
import kz.nrgteam.leetread.data.prefs.Prefs.Companion.PASSWORD
import kz.nrgteam.leetread.data.prefs.Prefs.Companion.USER_ID
import android.content.SharedPreferences
import javax.inject.Inject

class PrefsImpl @Inject constructor(
    private val preferences: SharedPreferences
) : Prefs {

    override fun getAccessToken(): String {
        return preferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    override fun setAccessToken(value: String) {
        with(preferences.edit()) {
            putString(ACCESS_TOKEN, value)
            commit()
        }
    }

    override fun getUserId(): Int {
        return preferences.getInt(USER_ID, -1)
    }

    override fun setUserId(value: Int) {
        with(preferences.edit()) {
            putInt(USER_ID, value)
            apply()
        }
    }

    override fun removeAccessToken() {
        with(preferences.edit()) {
            remove(ACCESS_TOKEN)
            apply()
        }
    }

    override fun removeUserId() {
        with(preferences.edit()) {
            remove(USER_ID)
            apply()
        }
    }

    override fun setBaseUrl(value: String) {
        with(preferences.edit()) {
            putString(BASE_URL, value)
            apply()
        }
    }

    override fun getBaseUrl(): String {
        return preferences.getString(BASE_URL, "") ?: ""
    }

    override fun setPassword(value: String) {
        with(preferences.edit()) {
            putString(PASSWORD, value)
            apply()
        }
    }

    override fun getPassword(): String {
        return preferences.getString(PASSWORD, "") ?: ""
    }

//    override fun setUser(user: User) {
//        val json = gson.toJson(user)
//        with(preferences.edit()){
//            putString(USER, json)
//            apply()
//        }
//    }
//
//    override fun getUser(): User {
//        val jsonOfUser = preferences.getString(USER, "")
//        return gson.fromJson(jsonOfUser, User::class.java)
//    }

}