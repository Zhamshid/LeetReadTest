package kz.nrgteam.leetread.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel : ViewModel() {

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    fun makeToast(string: String){
        launchIO {
            _toast.emit(string)
        }
    }

    // Do work in IO
    fun <P> launchIO(doOnAsyncBlock: suspend CoroutineScope.() -> P): Job {
        return viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
        }) {
            withContext(Dispatchers.IO) {
                doOnAsyncBlock.invoke(this)
            }
        }
    }

}