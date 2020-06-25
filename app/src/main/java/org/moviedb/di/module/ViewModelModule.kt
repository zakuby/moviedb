package org.moviedb.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.moviedb.di.ViewModelKey
import org.moviedb.ui.detail.DetailViewModel
import org.moviedb.ui.movie.MoviesViewModel
import org.moviedb.utils.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    internal abstract fun moviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun detailViewModel(viewModel: DetailViewModel): ViewModel
}
