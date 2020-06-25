package org.moviedb.data.remote

import io.reactivex.Single
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.models.Review
import org.moviedb.data.remote.response.BaseListResponse
import org.moviedb.data.remote.response.GenreResponse
import org.moviedb.data.remote.response.MovieCreditsResponse
import org.moviedb.data.remote.response.MovieTrailersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

interface TheMovieDbServices {

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Int): Single<Movie>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1
    ): Single<BaseListResponse<Movie>>

    @GET("discover/movie")
    fun getTodayReleaseMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("primary_release_date.gte") dateGte: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
        @Query("primary_release_date.lte") dateLte: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    ): Single<BaseListResponse<Movie>>

    @GET("search/movie")
    fun searchMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("query") keyword: String
    ): Single<BaseListResponse<Movie>>

    @GET("genre/movie/list")
    fun getMovieGenres(@Query("language") lang: String = "en-US"): Single<GenreResponse>

    @GET("discover/movie")
    fun searchByGenres(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("with_genres") genres: String
    ): Single<BaseListResponse<Movie>>

    @GET("movie/{id}/credits")
    fun getMovieCredits(
        @Path("id") id: Int
    ): Single<MovieCreditsResponse>

    @GET("movie/{id}/videos")
    fun getMovieVideos(
        @Path("id") id: Int
    ): Single<MovieTrailersResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): Single<BaseListResponse<Review>>

}