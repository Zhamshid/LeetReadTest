package kz.nrgteam.leetread.model.auth

data class UserResponse (
    val id : Int,
    val email : String?,
    val accessToken : String
)