package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.data.local.models.Genre
import org.moviedb.ui.base.BaseAdapter

class BottomFilterGenreListAdapter : BaseAdapter<Genre>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_genre_filter

    fun onCheck(item: Genre) {
        item.setCheck()
        notifyDataSetChanged()
    }

    fun getCheckedGenres(): String {
        val checkedItems = items.filter { item -> item.isChecked }
        return checkedItems.joinToString(separator = ",") { it.id.toString() }
    }
}
