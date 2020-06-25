package org.moviedb.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.moviedb.di.FragmentScoped
import org.moviedb.ui.SettingsFragment
import org.moviedb.ui.detail.DetailFragment
import org.moviedb.ui.movie.MoviesFragment

@Module
abstract class MainActivityModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeMoviesFragment(): MoviesFragment
}
