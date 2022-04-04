package kz.nrgteam.leetread.dto

import kz.nrgteam.leetread.model.auth.UserResponse

data class UserResponseDto(
    val id: Int,
    val email: String?,
    val first_name: String?,
    val last_name: String,
    val isAdmin: Boolean?,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?,
    val accessToken: String
) {
    fun toUserResponse() = UserResponse(
        id = id,
        email = email,
        accessToken = accessToken
    )
}