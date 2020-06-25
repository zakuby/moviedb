package org.moviedb.data.remote.response

import org.moviedb.data.local.models.Genre

data class GenreResponse(
    val genres: List<Genre>?
)
