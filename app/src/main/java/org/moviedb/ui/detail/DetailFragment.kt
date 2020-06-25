package org.moviedb.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import org.moviedb.R
import org.moviedb.adapters.DetailGenreListAdapter
import org.moviedb.adapters.DetailListCastAdapter
import org.moviedb.adapters.DetailReviewListAdapter
import org.moviedb.adapters.DetailVideoListAdapter
import org.moviedb.data.local.models.Cast
import org.moviedb.data.local.models.Movie
import org.moviedb.data.local.models.Review
import org.moviedb.data.local.models.Video
import org.moviedb.data.remote.Result
import org.moviedb.databinding.FragmentDetailBinding
import org.moviedb.ui.MainActivity
import org.moviedb.ui.WebViewActivity
import org.moviedb.ui.base.BaseFragment
import org.moviedb.utils.observe

class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailViewModel by viewModels { viewModelFactory }

    private val movieArgs: DetailFragmentArgs by navArgs()

    private val detailId: Int by lazy { movieArgs.id }

    private val castAdapter by lazy { DetailListCastAdapter() }

    private val videoAdapter by lazy { DetailVideoListAdapter(this::openYoutube) }

    private val genreAdapter by lazy { DetailGenreListAdapter() }

    private val reviewAdapter by lazy { DetailReviewListAdapter(this::viewFullReview) }

    private val activity: MainActivity by lazy { getActivity() as MainActivity }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            backButton.setOnClickListener { requireActivity().onBackPressed() }
            recyclerViewCast.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = castAdapter
            }
            recyclerViewVideo.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = videoAdapter
            }
            recyclerViewGenre.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = genreAdapter
            }
            recyclerViewReview.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = reviewAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        activity.supportActionBar?.show()
    }
    private fun subscribeUI() {
        viewModel.fetchDetail(detailId)
        observe(viewModel.casts, this::renderCasts)
        observe(viewModel.movie, this::renderMovieDetail)
        observe(viewModel.videos, this::renderMovieVideos)
        observe(viewModel.reviews, reviewAdapter::submitList)
        observe(viewModel.initialReviewEmpty, { isError ->
            binding.containerReview.isGone = isError
        })
        observe(viewModel.initialReviewLoading, { isLoading ->
            binding.shimmerViewReview.apply {
                isGone = !isLoading
                if (isLoading) startShimmer() else stopShimmer()
            }
            binding.reviewView.isGone = isLoading
        })
    }

    private fun renderCasts(result: Result<List<Cast>>) {
        when (result) {
            is Result.Success -> castAdapter.loadItems(result.data)
            is Result.Error -> binding.containerCast.isGone = true
            is Result.Loading -> binding.apply {
                shimmerViewCast.apply {
                    isGone = !result.isLoading
                    if (result.isLoading) startShimmer() else stopShimmer()
                }
                castView.isGone = result.isLoading
            }
        }
    }

    private fun renderMovieDetail(result: Result<Movie>) {
        when (result) {
            is Result.Success -> binding.apply {
                model = result.data
                genreAdapter.loadItems(result.data.genres ?: emptyList())
                executePendingBindings()
            }
            is Result.Loading -> binding.apply {
                shimmerViewDetail.apply {
                    isGone = !result.isLoading
                    if (result.isLoading) startShimmer() else stopShimmer()
                }
                parentDetail.isGone = result.isLoading
            }
        }
    }

    private fun renderMovieVideos(result: Result<List<Video>>) {
        when (result) {
            is Result.Success -> videoAdapter.loadItems(result.data)
            is Result.Error -> binding.containerVideos.isGone = true
            is Result.Loading -> binding.apply {
                shimmerViewVideos.apply {
                    isGone = !result.isLoading
                    if (result.isLoading) startShimmer() else stopShimmer()
                }
                videoView.isGone = result.isLoading
            }
        }
    }

    private fun viewFullReview(review: Review) {
        val i = Intent(activity, WebViewActivity::class.java).apply {
            putExtra("url", review.url)
            putExtra("title", "Review Detail by ${review.author}")
        }
        startActivity(i)
    }

    private fun openYoutube(key: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$key"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            requireContext().startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            requireContext().startActivity(webIntent)
        }
    }
}
