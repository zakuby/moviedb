package org.moviedb.ui.movie

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.moviedb.data.local.models.Genre
import org.moviedb.data.local.models.Movie
import org.moviedb.data.remote.TheMovieDbServices
import org.moviedb.data.remote.response.ErrorResponse
import org.moviedb.ui.base.BaseViewModel
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val ApiServices: TheMovieDbServices
) : BaseViewModel() {

    val initialLoading: LiveData<Boolean>
    val initialEmpty: LiveData<Boolean>
    val errorResponse: LiveData<ErrorResponse>
    val movies: LiveData<PagedList<Movie>>
    val searchQuery = ObservableField("")

    private val _movieGenres: MutableLiveData<List<Genre>> = MutableLiveData()
    val movieGenres: LiveData<List<Genre>> = _movieGenres

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        movies = MutableLiveData()//LivePagedListBuilder(dataSourceFactory, config).build()
        errorResponse = MutableLiveData()//Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getErrorResponse)
        initialLoading = MutableLiveData() //Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getInitialLoading)
        initialEmpty = MutableLiveData()// Transformations.switchMap(dataSourceFactory.getDataSource(), MovieDataSource::getInitialEmpty)
        fetchMovieGenres()
    }

//    fun retryLoadMovies() = dataSourceFactory.reloadInitial()

    private fun fetchMovieGenres() = ApiServices.getMovieGenres()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy { resp -> resp.genres?.let { _movieGenres.postValue(it) } }

    fun searchMovies(keyword: String?) {
        searchQuery.set(keyword)
//        dataSourceFactory.searchMovies(keyword)
    }

    fun searchByGenres(genres: String?) {
//        dataSourceFactory.searchMovies(genres, true)
    }
}