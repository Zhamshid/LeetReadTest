package kz.nrgteam.leetread.data.prefs

interface Prefs {

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val USER_ID = "USER_ID"
        const val BASE_URL = "BASE_URL"
        const val PASSWORD = "PASSWORD"
    }

    fun getAccessToken(): String
    fun setAccessToken(value: String)

    fun getUserId(): Int
    fun setUserId(value: Int)

    fun removeAccessToken()
    fun removeUserId()

    fun setBaseUrl(value: String)
    fun getBaseUrl(): String

    fun setPassword(value: String)
    fun getPassword(): String

//    fun setUser(user:User)
//    fun getUser():User

}