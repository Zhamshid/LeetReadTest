package kz.nrgteam.leetread.ui.all_books

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.kitaphana.Kitap
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AllBooksViewModel @Inject constructor(
    val repository: BaseCloudRepository,
    val prefs: Prefs,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<AllBooksUI>(AllBooksUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()
    val allBooksType = savedStateHandle.get<AllBooksOf>("all_books_type")
    val userId = savedStateHandle.get<Int>("user_id") ?: prefs.getUserId()
    init {
        when(allBooksType){
            AllBooksOf.ALL_BOOKS -> fetchAllBooks()
            AllBooksOf.FINISHED_BOOKS -> fetchMyFinishedBooks()
            null -> fetchAllBooks()
        }
    }

    private fun fetchAllBooks() {
        launchIO {
            when (val wrapper = repository.getAllBooks()) {
                is ResultWrapper.Error -> {
                    _stateFlow.value = AllBooksUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {
                    _stateFlow.value = AllBooksUI.Success(wrapper.value.map {
                        Kitap(
                            id = it.id,
                            name = it.title,
                            bookImage = it.coverFile,
                            authors = it.authors ?: listOf()
                        )
                    })
                }
            }
        }
    }
    private fun fetchMyFinishedBooks() {
        launchIO {
            when (val wrapper = repository.getMyLibraryBooks(userId)) {
                is ResultWrapper.Error -> {
                    _stateFlow.value = AllBooksUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {
                    _stateFlow.value = AllBooksUI.Success(wrapper.value.map {
                        Kitap(
                            id = it.id,
                            name = it.title,
                            bookImage = it.coverFile,
                            authors = it.authors ?: listOf()
                        )
                    })
                }
            }
        }
    }

}

sealed class AllBooksUI {
    object Loading : AllBooksUI()
    data class Success(val users: List<Kitap>) : AllBooksUI()
    class Error(val error: String?) : AllBooksUI()
}