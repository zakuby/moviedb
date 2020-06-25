package org.moviedb.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.PageKeyedDataSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.moviedb.data.local.models.Cast
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.models.Review
import org.moviedb.data.local.models.Video
import org.moviedb.data.remote.ApiCallHelper
import org.moviedb.data.remote.Result
import org.moviedb.data.remote.TheMovieDbServices
import org.moviedb.data.remote.response.ErrorResponse

@Singleton
class DetailRepository @Inject constructor(
    private val services: TheMovieDbServices
) {

    fun fetchDetail(scope: CoroutineScope, id: Int): LiveData<Result<Movie>> = liveData(scope.coroutineContext) {
        emit(Result.Loading(true))
        when (val resp = ApiCallHelper.getResult { services.getMovieDetail(id) }) {
            is Result.Success -> {
                emit(Result.Loading(false))
                emit(Result.Success(resp.data))
            }
            is Result.Error -> {
                emit(Result.Loading(false))
                emit(Result.Error(resp.error))
            }
        }
    }

    fun fetchMovieCast(scope: CoroutineScope, id: Int): LiveData<Result<List<Cast>>> = liveData(scope.coroutineContext) {
        emit(Result.Loading(true))
        when (val resp = ApiCallHelper.getResult { services.getMovieCredits(id) }) {
            is Result.Success -> {
                emit(Result.Loading(false))
                if (!resp.data.cast.isNullOrEmpty()) {
                    emit(Result.Success(resp.data.cast))
                } else {
                    emit(Result.Error(ErrorResponse(404, "Data is empty")))
                }
            }
            is Result.Error -> {
                emit(Result.Loading(false))
                emit(Result.Error(ErrorResponse(404, "Data is empty")))
            }
        }
    }

    fun fetchMovieTrailer(scope: CoroutineScope, id: Int): LiveData<Result<List<Video>>> = liveData(scope.coroutineContext) {
        emit(Result.Loading(true))
        when (val resp = ApiCallHelper.getResult { services.getMovieVideos(id) }) {
            is Result.Success -> {
                emit(Result.Loading(false))
                if (!resp.data.results.isNullOrEmpty()) {
                    emit(Result.Success(resp.data.results))
                } else {
                    emit(Result.Error(ErrorResponse(404, "Data is empty")))
                }
            }
            is Result.Error -> {
                emit(Result.Loading(false))
                emit(Result.Error(ErrorResponse(404, "Data is empty")))
            }
        }
    }

    fun fetchReviewsInitial(
        scope: CoroutineScope,
        id: Int,
        initialEmpty: MutableLiveData<Boolean>,
        initialLoading: MutableLiveData<Boolean>,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, Review>
    ) {
        scope.launch {
            initialLoading.postValue(true)
            when (val resp = ApiCallHelper.getResult { services.getMovieReviews(id) }) {
                is Result.Success -> {
                    initialLoading.postValue(false)
                    if (!resp.data.results.isNullOrEmpty()) {
                        callback.onResult(resp.data.results, null, 2)
                        initialEmpty.postValue(false)
                    } else initialEmpty.postValue(true)
                }
                is Result.Error -> {
                    initialLoading.postValue(false)
                    initialEmpty.postValue(true)
                }
            }
        }
    }

    fun fetchReviews(
        scope: CoroutineScope,
        page: Int,
        id: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, Review>
    ) {
        scope.launch {
            when (val resp = ApiCallHelper.getResult { services.getMovieReviews(id, page) }) {
                is Result.Success -> {
                    resp.data.results ?: return@launch
                    callback.onResult(resp.data.results, page + 1)
                }
            }
        }
    }
}
