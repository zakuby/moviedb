package org.moviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.repository.MovieRepository
import org.moviedb.data.local.source.MovieDataSource.SourceType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDataSourceFactory @Inject constructor(private val repository: MovieRepository) : DataSource.Factory<Int, Movie>() {

    private lateinit var dataSource: MovieDataSource

    private val dataSourceLiveData = MutableLiveData<MovieDataSource>()

    private val scope = CoroutineScope(Dispatchers.IO)

    private var sourceType: SourceType = SourceType.DEFAULT

    private var keywords: String? = ""

    override fun create(): DataSource<Int, Movie> {
        dataSource = MovieDataSource(repository, scope, sourceType, keywords ?: "")
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun reloadInitial() = dataSource.invalidate()

    fun searchMovies(keyword: String?, isByGenre: Boolean = false) {
        this.keywords = keyword ?: ""
        this.sourceType =
            if (isByGenre) SourceType.BY_GENRES else if (keyword.isNullOrEmpty()) SourceType.DEFAULT else SourceType.BY_SEARCH_QUERY
        dataSource.invalidate()
    }

    fun getKeywords(): String = if (sourceType == SourceType.BY_SEARCH_QUERY) keywords ?: "" else ""

    fun getDataSource(): LiveData<MovieDataSource> = dataSourceLiveData

    fun onClear() = scope.cancel()
}