package kz.nrgteam.leetread.ui.kitap

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.cloud.repository.CloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangeReadPageViewModel @Inject constructor(
    private val repository: BaseCloudRepository,
    savedStateHandle: SavedStateHandle,
    prefs: Prefs
) : BaseViewModel() {
    private val _book = savedStateHandle.get<Book>("book")
    val book get() = _book!!
    var lastPageNumber = book.page_count_progress

    private fun getLastPage(lastPage: String?): Int {
        return try {
            (lastPage ?: "0").toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun setLastPage(i: String) {
        lastPageNumber = try {
            (i).toInt()
        } catch (e: Exception) {
            0
        }
    }

    var allPageCounts = book.pageCount ?: 0
    val userId = prefs.getUserId()

    private val _stateFlow = MutableStateFlow<ChangeLastPageUI>(ChangeLastPageUI.NotLoading)
    val stateFlow = _stateFlow.asStateFlow()


    fun changeLastReadPage() {
        launchIO {
            val b = CloudRepository.BookLastPage(
                userId.toString(),
                book.id.toString(),
                0.0,
                lastPageNumber - (book.pageCount?:0),
                "",
                0,
                lastPageNumber
            )
            when (val wrapper = repository.changeLastReadPageNumber(
                b
            )) {
                is ResultWrapper.Error -> {
                    _stateFlow.value = ChangeLastPageUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {
                    _stateFlow.value = ChangeLastPageUI.Success("Success")
                }
            }
        }
    }
}

sealed class ChangeLastPageUI {
    object NotLoading : ChangeLastPageUI()
    data class Success(val value: String) : ChangeLastPageUI()
    class Error(val error: String?) : ChangeLastPageUI()
}
