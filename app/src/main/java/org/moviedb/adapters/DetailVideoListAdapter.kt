package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.data.local.models.Video
import org.moviedb.ui.base.BaseAdapter

class DetailVideoListAdapter(private val onClickPlay: (String) -> Unit) : BaseAdapter<Video>() {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_video

    fun playButtonListener(youtubeUrl: String) = onClickPlay(youtubeUrl)
}