package kz.nrgteam.leetread.ui.rating

import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val repository: BaseCloudRepository
) : BaseViewModel() {

    val state: StateFlow<String>
    val accept: (String) -> Unit
    val pagingDataFlow: Flow<PagingData<Follower>>

    init {
        val actionStateFlow = MutableSharedFlow<String>()
        val searches = actionStateFlow
            .filterIsInstance<String>()
            .distinctUntilChanged()
            .onStart { }
        state = searches.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = ""
        )

        pagingDataFlow = searches
            .flatMapLatest { getSearchedUsers(searchedText) }
            .cachedIn(viewModelScope)

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    var searchedText: String = ""

    private fun getSearchedUsers(text: String): Flow<PagingData<Follower>> = repository.getSearchedUsers(
        text
    )

}