package org.moviedb.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.dlg_bottom_filter.*
import org.moviedb.R
import org.moviedb.adapters.BottomFilterGenreListAdapter
import org.moviedb.adapters.MovieListAdapter
import org.moviedb.data.local.models.Genre
import org.moviedb.data.local.models.Movie
import org.moviedb.data.remote.Result
import org.moviedb.databinding.FragmentMoviesBinding
import org.moviedb.ui.base.BaseFragment
import org.moviedb.utils.observe
import javax.inject.Inject

class MoviesFragment : BaseFragment<FragmentMoviesBinding>(R.layout.fragment_movies) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels { viewModelFactory }

    private val adapter by lazy { MovieListAdapter { movie -> navigateToDetail(movie) } }

    private val genreAdapter by lazy { BottomFilterGenreListAdapter() }

    private fun retryLoadMovie() = viewModel.retryLoadMovies()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        setupSearchView()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            errorLayout.retryButton.setOnClickListener { retryLoadMovie() }
            viewModel = this@MoviesFragment.viewModel
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@MoviesFragment.adapter
            }
            btnFilter.setOnClickListener { showBottomFilter() }
        }
    }

    private fun setupSearchView(){
        binding.searchView.apply {
            val query = this@MoviesFragment.viewModel.searchQuery.get()
            setQuery(query, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    this@MoviesFragment.viewModel.searchMovies(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
            view?.findViewById<ImageView>(R.id.search_close_btn)?.setOnClickListener {
                this@MoviesFragment.viewModel.searchMovies("")
                setQuery("", false)
                isIconified = true
            }
        }
    }

    private fun subscribeUI() {
        observe(viewModel.movies, adapter::submitList)
        observe(viewModel.genres, { result ->
            when(result){
                is Result.Success -> setButtonFilterListener(result.data)
            }
        })
    }

    private fun navigateToDetail(movie: Movie){
        val action = MoviesFragmentDirections.actionBottomNavFragmentToDetailFragment(movie.id)
        findNavController().navigate(action)
    }

    private fun setButtonFilterListener(genres: List<Genre>) = genreAdapter.loadItems(genres)

    private fun showBottomFilter() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.dlg_bottom_filter, bottom_sheet_container).apply {
            findViewById<RecyclerView>(R.id.recycler_view_genre).apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = this@MoviesFragment.genreAdapter
            }
            findViewById<MaterialButton>(R.id.btn_add_apply).setOnClickListener {
                bottomSheetDialog.dismiss()
                binding.searchView.apply {
                    setQuery("", false)
                    isIconified = true
                }
                viewModel.searchByGenres(genreAdapter.getCheckedGenres())
            }
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}