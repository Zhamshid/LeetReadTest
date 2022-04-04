package kz.nrgteam.leetread.ui.followers_list

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.CloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel
@Inject constructor(
    val prefs: Prefs,
    val repository: CloudRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val myActuallyID = prefs.getUserId()
    val userId: Int = savedStateHandle["userId"] ?: myActuallyID
    var isFollowers = savedStateHandle.get<Boolean>("isFollowers") ?: false

    private val _stateFlow = MutableStateFlow<FollowersUI>(FollowersUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        _stateFlow.value = FollowersUI.Loading
        fetchUsers()
    }

    fun fetchUsers() {
        if (isFollowers) {
            fetchFollowers()
        } else {
            fetchFollowings()
        }
    }

    fun subscribe(id: Int) {
        launchIO {
            repository.follow(id)
        }
    }

    fun unsubscribe(id: Int) {
        launchIO {
            repository.unFollow(id)
        }
    }

    private fun fetchFollowers() {
        launchIO {
            when (val wrapper = repository.getFollowers(userId)) {
                is ResultWrapper.Success -> {
                    wrapper.value.let { list ->
                        _stateFlow.value = FollowersUI.Success(
                            list.map {
                                it.isInFollowing = it.is_follow != 0
                                it
                            })
                    }
                }
                is ResultWrapper.Error -> {
                    Log.i(TAG, "fetchFollowers: ${wrapper.code}")
                    _stateFlow.value = FollowersUI.Error(wrapper.error)
                    makeToast(wrapper.error.toString())
                }
            }
        }
    }

    private fun fetchFollowings() {
        launchIO {
            when (val wrapper = repository.getFollowings(userId)) {
                is ResultWrapper.Success -> {
                    wrapper.value.let { list ->
                        _stateFlow.value = FollowersUI.Success(list.map {
                            it.isInFollowing = it.is_follow != 0
                            it
                        })
                    }
                }
                is ResultWrapper.Error -> {
                    Log.i(TAG, "fetchFollowings: ${wrapper.code}")
                    _stateFlow.value = FollowersUI.Error(wrapper.error)
                    makeToast(wrapper.error.toString())
                }
            }
        }
    }

    fun refresh() {
        _stateFlow.value = FollowersUI.Default
        fetchUsers()
    }

    companion object {
        const val TAG = "ABO_FOLLOWERS_VM"
    }

}

sealed class FollowersUI {
    object Loading : FollowersUI()
    object Default : FollowersUI()
    data class Success(val value: List<Follower>) : FollowersUI()
    class Error(val error: String?) : FollowersUI()
}