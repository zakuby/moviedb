package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.data.local.models.Movie
import org.moviedb.ui.base.BasePagedListAdapter

class MovieListAdapter(
    clickListener: (Movie) -> Unit
) : BasePagedListAdapter<Movie>(clickListener, Movie.DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_movie
}
