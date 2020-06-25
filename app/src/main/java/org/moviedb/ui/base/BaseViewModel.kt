package org.moviedb.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {
    private val loading = MutableLiveData<Boolean>()

    fun getLoading(): LiveData<Boolean> = loading

    protected val scope = CoroutineScope(Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}