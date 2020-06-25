package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.data.local.models.Genre
import org.moviedb.ui.base.BaseAdapter

class DetailGenreListAdapter : BaseAdapter<Genre>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_genre
}
