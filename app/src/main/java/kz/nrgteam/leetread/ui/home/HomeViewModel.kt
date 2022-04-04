package kz.nrgteam.leetread.ui.home

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.user.UserOnline
import kz.nrgteam.leetread.ui.home.adapters.HomeChildVHUI
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: BaseCloudRepository,
    val prefs: Prefs
) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<NewsUI>(NewsUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        fetchNewsAndOnlineUsers()
    }

    fun getNews(): Flow<PagingData<HomeChildVHUI>> {
        return repository.getNews()
            .cachedIn(viewModelScope)
    }

    private fun fetchNewsAndOnlineUsers() {
        _stateFlow.value = NewsUI.Loading
        launchIO {
            val onlineUsers =
                repository.getFollowings(prefs.getUserId())
            when (onlineUsers) {
                is ResultWrapper.Error -> {}
                is ResultWrapper.Success -> {
                    val users =
                        onlineUsers.value.map {
                            it.isInFollowing = it.is_follow != 0
                            it
                        }.map {
                            UserOnline(
                                it.following_id,
                                it.cover_file,
                                true
                            )
                        }
                    _stateFlow.value = NewsUI.Success(users)
                }
            }

        }
    }

    fun retry() {
        _stateFlow.value = NewsUI.Loading
        fetchNewsAndOnlineUsers()
    }

    fun refresh() {
        fetchNewsAndOnlineUsers()
    }

    companion object {
        const val TAG = "ABO_HOME_MODEL_TAG"
    }
}

sealed class NewsUI {
    object Loading : NewsUI()
    data class Success(val users: List<UserOnline>) : NewsUI()
    class Error(val error: String?) : NewsUI()
}