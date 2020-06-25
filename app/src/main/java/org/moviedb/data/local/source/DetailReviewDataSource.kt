package org.moviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import org.moviedb.data.local.models.Review
import org.moviedb.data.local.repository.DetailRepository

class DetailReviewDataSource constructor(
    private val scope: CoroutineScope,
    private val repository: DetailRepository,
    private val id: Int
) : PageKeyedDataSource<Int, Review>() {

    private val initialLoading = MutableLiveData<Boolean>().apply { postValue(false) }

    private val initialEmpty = MutableLiveData<Boolean>().apply { postValue(false) }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Review>
    ) = repository.fetchReviewsInitial(scope, id, initialEmpty, initialLoading, callback)

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Review>
    ) = repository.fetchReviews(scope, params.key + 1, id, callback)

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Review>) {}

    fun getInitialLoading(): LiveData<Boolean> = initialLoading
    fun getInitialEmpty(): LiveData<Boolean> = initialEmpty
}
