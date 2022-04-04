package kz.nrgteam.leetread.data.cloud

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.dto.response.ErrorResponse
import kz.nrgteam.leetread.dto.response.ErrorStatus
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            val req = apiCall.invoke()
            if (req.isSuccessful && req.body() != null) ResultWrapper.Success(req.body()!!)
            else ResultWrapper.Error(code = req.code(), error = req.message().toString())
        } catch (throwable: Throwable) {
            when (throwable) {
                is UnknownHostException -> {
                    ResultWrapper.Error(
                        ErrorStatus.NO_CONNECTION, null, kz.nrgteam.leetread.core.App.instance.getString(
                            R.string.no_internet_connection
                        )
                    )
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse =
                        convertErrorBody(
                            throwable
                        )
                    ResultWrapper.Error(ErrorStatus.BAD_RESPONSE, code, errorResponse)
                }
                is SocketTimeoutException -> {
                    ResultWrapper.Error(
                        ErrorStatus.TIMEOUT,
                        null,
                        kz.nrgteam.leetread.core.App.instance.getString(R.string.socket_timeout_exception)
                    )
                }
                else -> {
                    ResultWrapper.Error(ErrorStatus.NOT_DEFINED, 4499, throwable.message)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()?.let {
            Gson().fromJson(it, ErrorResponse::class.java).error
        }
    } catch (exception: Exception) {
        null
    }
}