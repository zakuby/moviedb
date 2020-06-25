package org.moviedb.data.remote

import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.models.Review
import org.moviedb.data.remote.response.BaseListResponse
import org.moviedb.data.remote.response.GenreResponse
import org.moviedb.data.remote.response.MovieCreditsResponse
import org.moviedb.data.remote.response.MovieTrailersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbServices {

    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("language") lang: String = "en-US"): Response<GenreResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<BaseListResponse<Movie>>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int,
        @Query("query") keyword: String
    ): Response<BaseListResponse<Movie>>

    @GET("discover/movie")
    suspend fun searchByGenres(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int,
        @Query("with_genres") genres: String
    ): Response<BaseListResponse<Movie>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): Response<Movie>

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: Int
    ): Response<MovieCreditsResponse>

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int
    ): Response<MovieTrailersResponse>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): Response<BaseListResponse<Review>>
}
