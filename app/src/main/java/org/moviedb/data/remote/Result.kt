package org.moviedb.data.remote

import org.moviedb.data.remote.response.ErrorResponse

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val data: T) : Result<T>()
    data class Error(val error: ErrorResponse) : Result<Nothing>()
    data class Loading(val isLoading: Boolean) : Result<Nothing>()
}