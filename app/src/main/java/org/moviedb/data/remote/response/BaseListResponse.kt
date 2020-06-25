package org.moviedb.data.remote.response

data class BaseListResponse<T>(
    val page: Int?,
    val results: List<T>?
)
