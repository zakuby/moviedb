package org.moviedb.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import org.moviedb.data.local.models.Cast
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.models.Video
import org.moviedb.data.remote.ApiCallHelper
import org.moviedb.data.remote.TheMovieDbServices
import org.moviedb.data.remote.Result
import org.moviedb.data.remote.response.ErrorResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val services: TheMovieDbServices
){

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
                if (!resp.data.cast.isNullOrEmpty()){
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
                if (!resp.data.results.isNullOrEmpty()){
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


}