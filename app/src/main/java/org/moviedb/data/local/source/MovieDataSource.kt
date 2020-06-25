package org.moviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.repository.MovieRepository
import org.moviedb.data.local.source.MovieDataSource.SourceType.DEFAULT
import org.moviedb.data.remote.response.ErrorResponse

class MovieDataSource constructor(
    private val repository: MovieRepository,
    private val scope: CoroutineScope,
    private val sourceType: SourceType = DEFAULT,
    private val keywords: String = ""
) : PageKeyedDataSource<Int, Movie>() {

    enum class SourceType {
        BY_SEARCH_QUERY,
        BY_GENRES,
        DEFAULT
    }

    private val initialLoading = MutableLiveData<Boolean>()

    private val errorResponse = MutableLiveData<ErrorResponse>()

    private val initialEmpty = MutableLiveData<Boolean>().apply { postValue(false) }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) =
        repository.fetchMovieInitial(scope, sourceType, 1, keywords, callback, errorResponse, initialLoading, initialEmpty)


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>)  =
        repository.fetchMovie(scope, sourceType, params.key, keywords, callback)

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    fun getInitialLoading(): LiveData<Boolean> = initialLoading
    fun getInitialEmpty(): LiveData<Boolean> = initialEmpty
    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse
}