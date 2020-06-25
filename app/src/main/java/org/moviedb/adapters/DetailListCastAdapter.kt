package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.ui.base.BaseAdapter
import org.moviedb.data.local.models.Cast

class DetailListCastAdapter : BaseAdapter<Cast>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_cast
}