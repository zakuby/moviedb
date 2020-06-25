package org.moviedb.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.moviedb.data.local.models.Genre
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.source.MovieDataSource.SourceType
import org.moviedb.data.remote.ApiCallHelper
import org.moviedb.data.remote.Result
import org.moviedb.data.remote.TheMovieDbServices
import org.moviedb.data.remote.response.ErrorResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val services: TheMovieDbServices) {

    fun fetchGenre(scope: CoroutineScope): LiveData<Result<List<Genre>>> =
        liveData(scope.coroutineContext) {
            when (val resp = ApiCallHelper.getResult { services.getMovieGenres() }) {
                is Result.Success -> emit(Result.Success(resp.data.genres!!))
                is Result.Error -> emit(Result.Error(resp.error))
            }
        }

    fun fetchMovieInitial(
        scope: CoroutineScope,
        type: SourceType,
        page: Int = 1,
        keywords: String = "",
        callback: PageKeyedDataSource.LoadInitialCallback<Int, Movie>,
        errorResponse: MutableLiveData<ErrorResponse>,
        initialLoading: MutableLiveData<Boolean>,
        initialEmpty: MutableLiveData<Boolean>
    ) {
        scope.launch {
            initialLoading.postValue(true)
            val service = when (type) {
                SourceType.BY_SEARCH_QUERY -> services.searchMovies(page = page, keyword = keywords)
                SourceType.BY_GENRES -> services.searchByGenres(page = page, genres = keywords)
                SourceType.DEFAULT -> services.getPopularMovies(page = page)
            }

            when (val resp = ApiCallHelper.getResult { service }) {
                is Result.Success -> {
                    initialLoading.postValue(false)
                    if (!resp.data.results.isNullOrEmpty()) {
                        callback.onResult(resp.data.results, null, 2)
                        initialEmpty.postValue(false)
                    } else initialEmpty.postValue(true)
                }
                is Result.Error -> {
                    errorResponse.postValue(resp.error)
                    initialLoading.postValue(false)
                    initialEmpty.postValue(true)
                }
            }
        }
    }


    fun fetchMovie(
        scope: CoroutineScope,
        type: SourceType,
        page: Int,
        keywords: String = "",
        callback: PageKeyedDataSource.LoadCallback<Int, Movie>
    ) {
        scope.launch {
            val service = when (type) {
                SourceType.BY_SEARCH_QUERY -> services.searchMovies(page = page, keyword = keywords)
                SourceType.BY_GENRES -> services.searchByGenres(page = page, genres = keywords)
                SourceType.DEFAULT -> services.getPopularMovies(page = page)
            }

            when (val resp = ApiCallHelper.getResult { service }) {
                is Result.Success -> {
                    resp.data.results ?: return@launch
                    callback.onResult(resp.data.results, page + 1)
                }
            }
        }
    }

}