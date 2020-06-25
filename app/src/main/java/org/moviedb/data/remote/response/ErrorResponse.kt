package org.moviedb.data.remote.response

data class ErrorResponse(
    val code: Int = 500,
    val message: String? = "Internal Server Error.",
    val type: Type? = Type.GENERAL
) {
    enum class Type {
        GENERAL,
        HOTSPOT_LOGIN,
        NO_INTERNET_CONNECTION,
        REQUEST_TIMEOUT
    }
}
