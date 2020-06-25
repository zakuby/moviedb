package org.moviedb.adapters

import org.moviedb.ui.base.BaseAdapter
import org.moviedb.R
import org.moviedb.data.local.models.Genre

class DetailGenreListAdapter : BaseAdapter<Genre>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_genre
}