package kz.nrgteam.leetread.ui.kitap

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.cloud.repository.CloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.kitaphana.BookItem
import kz.nrgteam.leetread.model.UserX
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KitapViewModel @Inject constructor(
    private val repository: BaseCloudRepository,
    savedStateHandle: SavedStateHandle,
    prefs: Prefs
) : BaseViewModel() {
    private val _bookId = savedStateHandle.get<Int>("book_id")
    val bookId get() = _bookId!!
    var book: BookItem? = null
    val userId = prefs.getUserId()
    var bookLastPage = 0
    var bookPageProgress = book?.libraryInfo?.page_count_progress ?: 0

    private val _stateFlow = MutableStateFlow<KitapUI>(KitapUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        _stateFlow.value = KitapUI.Loading
    }

    fun fetchBook() {
        launchIO {
            when (val wrapper = repository.getBook(bookId)) {
                is ResultWrapper.Success -> {
                    Log.i("kitapVMSuccess", wrapper.value.toString())
                    wrapper.value.let {
                        book = it
                        bookPageProgress = book?.libraryInfo?.page_count_progress ?: 0
                        bookLastPage = book?.libraryInfo?.page_count_progress ?: 0
                        _stateFlow.value = KitapUI.Success(it)
                    }
                }
                is ResultWrapper.Error -> {
                    _stateFlow.value = KitapUI.Error(wrapper.error)
                    Log.i("fail", wrapper.error.toString())
                    makeToast(wrapper.error.toString())
                }
            }
        }
    }

    fun setLastPage(i: String) {
        bookLastPage = try {
            (i).toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun changeLastReadPage() {
        launchIO {
            Log.i("ABBO", "changeLastReadPage: $bookLastPage, $bookPageProgress")
            if (bookLastPage == bookPageProgress) return@launchIO
            val b = CloudRepository.BookLastPage(
                userId.toString(),
                book?.book?.id.toString(),
                (bookLastPage.div(book?.book?.pageCount?.toDouble() ?: 1.0)) * 100,
                bookLastPage - (bookPageProgress),
                book?.libraryInfo?.last_page ?: "",
                0,
                totalPageCount = bookLastPage
            )
            when (val wrapper = repository.changeLastReadPageNumber(
                b
            )) {
                is ResultWrapper.Error -> {
                    _stateFlow.value = KitapUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {

                }
            }
        }
    }

    val usersDaily = MutableLiveData(ArrayList<UserX>())

    fun refresh() {
        _stateFlow.value = KitapUI.NotLoading
        fetchBook()
    }

    fun retry() {
        _stateFlow.value = KitapUI.Loading
        fetchBook()
    }

}

sealed class KitapUI {
    object Loading : KitapUI()
    object NotLoading : KitapUI()
    data class Success(val value: BookItem) : KitapUI()
    class Error(val error: String?) : KitapUI()
}