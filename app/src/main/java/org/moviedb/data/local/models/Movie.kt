package org.moviedb.data.local.models

import android.content.ContentValues
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String?,
    @SerializedName("release_date")
    val date: String?,
    @SerializedName("overview")
    val description: String?,
    @SerializedName("vote_average")
    val rate: String?,
    @SerializedName("poster_path")
    val posterImage: String?,
    @SerializedName("backdrop_path")
    val backgroundImage: String?,
    val isMovie: Boolean? = true,
    var isFavorite: Boolean = false,
    val genres: List<Genre>? = emptyList()
) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}