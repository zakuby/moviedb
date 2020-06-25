package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.data.local.models.Cast
import org.moviedb.ui.base.BaseAdapter

class DetailListCastAdapter : BaseAdapter<Cast>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_cast
}
