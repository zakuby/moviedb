package org.moviedb.ui.detail

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import org.moviedb.data.local.models.*
import org.moviedb.data.remote.TheMovieDbServices
import org.moviedb.ui.base.BaseViewModel
import org.moviedb.utils.SingleLiveEvent
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val service: TheMovieDbServices
) : BaseViewModel() {

    private val casts = MutableLiveData<List<Cast>>()
    fun getMovieCasts(): LiveData<List<Cast>> = casts
    private val videos = MutableLiveData<List<Video>>()
    fun getDetailVideos(): LiveData<List<Video>> = videos
    private val genres = MutableLiveData<List<Genre>>()
    fun getDetailGenre(): LiveData<List<Genre>> = genres

    val onReviewLiveDataReady = SingleLiveEvent<Unit>()
    private lateinit var reviews: LiveData<PagedList<Review>>
    fun getDetailReview(): LiveData<PagedList<Review>> = reviews

    private val _isMovie = ObservableBoolean(true)
    private val isMovie get() = _isMovie.get()

    val movie = MutableLiveData<Movie>()
    private val movieData get() = movie.value

    val isMovieFavorite = ObservableBoolean(false)
    private val isFavorite get() = isMovieFavorite.get()

    val movieFavoriteLoading = ObservableBoolean(true)
    val detailCastLoading = ObservableBoolean(false)
    val detailVideosLoading = ObservableBoolean(false)
    lateinit var detailReviewLoading: LiveData<Boolean>

    val isErrorCast = ObservableBoolean(false)
    val isErrorVideo = ObservableBoolean(false)

    lateinit var isErrorReview: LiveData<Boolean>

}