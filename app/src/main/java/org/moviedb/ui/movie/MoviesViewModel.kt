package org.moviedb.ui.movie

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.repository.MovieRepository
import org.moviedb.data.local.source.MovieDataSource
import org.moviedb.data.local.source.MovieDataSourceFactory
import org.moviedb.data.remote.response.ErrorResponse
import org.moviedb.ui.base.BaseViewModel
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    repository: MovieRepository,
    private val dataSourceFactory: MovieDataSourceFactory
) : BaseViewModel() {

    val initialLoading: LiveData<Boolean>
    val initialEmpty: LiveData<Boolean>
    val errorResponse: LiveData<ErrorResponse>
    val movies: LiveData<PagedList<Movie>>
    val searchQuery = ObservableField(dataSourceFactory.getKeywords())

    val genres = repository.fetchGenre(scope)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        movies = LivePagedListBuilder(dataSourceFactory, config).build()
        errorResponse = Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getErrorResponse)
        initialLoading = Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getInitialLoading)
        initialEmpty = Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getInitialEmpty)
    }

    fun retryLoadMovies() = dataSourceFactory.reloadInitial()

    fun searchMovies(keyword: String?) {
        searchQuery.set(keyword)
        dataSourceFactory.searchMovies(keyword)
    }

    fun searchByGenres(genres: String?) {
        dataSourceFactory.searchMovies(genres, true)
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.onClear()
    }
}