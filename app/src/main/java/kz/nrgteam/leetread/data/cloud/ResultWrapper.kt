package kz.nrgteam.leetread.data.cloud

import kz.nrgteam.leetread.dto.response.ErrorStatus


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(
        val status: ErrorStatus? = null,
        val code: Int? = null,
        val error: String? = null
    ) : ResultWrapper<Nothing>()
}