package org.moviedb.data.remote

import org.moviedb.data.remote.response.ErrorResponse
import retrofit2.Response

object ApiCallHelper {
    suspend fun <T: Any> getResult(call: suspend () -> Response<T>): Result<T>{
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Result.Success(body)
                }?: return Result.Error(ErrorResponse(500, "Response is empty"))
            } else {
                return Result.Error(ErrorResponse(response.code(), response.errorBody().toString()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(ErrorResponse(500, e.message ?: "Internal Server Error"))
        }
    }
}