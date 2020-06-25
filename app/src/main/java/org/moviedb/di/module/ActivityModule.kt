package org.moviedb.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.moviedb.di.ActivityScoped
import org.moviedb.ui.MainActivity
import org.moviedb.ui.WebViewActivity

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeWebViewActivity(): WebViewActivity
}
