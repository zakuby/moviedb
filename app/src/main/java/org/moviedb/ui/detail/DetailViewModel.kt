package org.moviedb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import javax.inject.Inject
import org.moviedb.data.local.models.*
import org.moviedb.data.local.repository.DetailRepository
import org.moviedb.data.local.source.DetailReviewDataSource
import org.moviedb.data.local.source.DetailReviewDataSourceFactory
import org.moviedb.data.remote.Result
import org.moviedb.ui.base.BaseViewModel

class DetailViewModel @Inject constructor(
    private val repository: DetailRepository
) : BaseViewModel() {

    private var id: Int = 0

    fun fetchDetail(id: Int) {
        this.id = id
        movie = repository.fetchDetail(scope, id)
        casts = repository.fetchMovieCast(scope, id)
        videos = repository.fetchMovieTrailer(scope, id)
        fetchReviews()
    }

    private fun fetchReviews() {

        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        val dataSource = DetailReviewDataSource(scope, repository, id)
        val dataSourceFactory = DetailReviewDataSourceFactory(dataSource)
        reviews = LivePagedListBuilder(dataSourceFactory, config).build()
        initialReviewLoading = Transformations.switchMap(dataSourceFactory.getDataSource(), DetailReviewDataSource::getInitialLoading)
        initialReviewEmpty = Transformations.switchMap(dataSourceFactory.getDataSource(), DetailReviewDataSource::getInitialEmpty)
    }

    lateinit var movie: LiveData<Result<Movie>>
    lateinit var casts: LiveData<Result<List<Cast>>>
    lateinit var videos: LiveData<Result<List<Video>>>
    lateinit var reviews: LiveData<PagedList<Review>>
    lateinit var initialReviewLoading: LiveData<Boolean>
    lateinit var initialReviewEmpty: LiveData<Boolean>
}
