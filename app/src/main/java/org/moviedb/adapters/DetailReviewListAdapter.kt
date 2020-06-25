package org.moviedb.adapters

import org.moviedb.R
import org.moviedb.data.local.models.Review
import org.moviedb.ui.base.BasePagedListAdapter

class DetailReviewListAdapter(
    clickListener: (Review) -> Unit
) : BasePagedListAdapter<Review>(clickListener, diffUtil = Review.DIFF_CALLBACK) {
    override val getLayoutIdRes: Int
        get() = R.layout.list_item_review
}