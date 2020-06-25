package org.moviedb.data.remote.response

import org.moviedb.data.local.models.Video

data class MovieTrailersResponse(
    val results: List<Video>?
)
