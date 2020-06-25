package org.moviedb.data.remote.response

import org.moviedb.data.local.models.Cast

class MovieCreditsResponse(
    val id: Int?,
    val cast: List<Cast>?
)