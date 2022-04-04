package kz.nrgteam.leetread.ui.zharystarList

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.ui.rating.ratingTable.RatingUI
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ZharystarListViewModel @Inject constructor(
    val baseCloudRepository: BaseCloudRepository,
    savedStateHandle: SavedStateHandle,
    prefs: Prefs
) : BaseViewModel() {

    val zharystarTiming: Int? = savedStateHandle["zharystar_timing"]
    private val _stateFlow = MutableStateFlow<RatingUI>(RatingUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()
    val userId = prefs.getUserId()
    var currentUser: Follower? = null

    init {
        fetchUsers()
    }

    private fun fetchUsers(){
        _stateFlow.value = RatingUI.Loading
        when (zharystarTiming) {
            1 -> {
                fetchUsersDaily()
            }
            2 -> {
                fetchUsersWeekly()
            }
            3 -> {
                fetchUsersMonthly()
            }
        }
    }

    private fun fetchUsersDaily() {
        launchIO {
            when (val wrapper = baseCloudRepository.getDailyChallenge()) {
                is ResultWrapper.Error -> {
                    Log.i(TAG, "fetchUsersDaily: ERROR")
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                    makeToast(wrapper.error.toString())
                }
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        Log.i(TAG, "fetchUsersDaily: Success")
                        val a = it.mapIndexed { index, follower ->
                            if (follower.user_id == userId) {
                                currentUser = follower
                            }
                            follower._id = index + 1
                            follower
                        }
                        _stateFlow.value = RatingUI.Success(a)
                    }
                }
            }
        }
    }

    private fun fetchUsersWeekly() {
        launchIO {
            when (val wrapper = baseCloudRepository.getWeeklyChallenge()) {
                is ResultWrapper.Error -> {
                    Log.i(TAG, "fetchUsersWeekly: ERROR")
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                    makeToast(wrapper.error.toString())
                }
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        Log.i(TAG, "fetchUsersWeekly: Success")
                        val a = it.mapIndexed { index, follower ->
                            if (follower.user_id == userId) {
                                currentUser = follower
                            }
                            follower._id = index + 1
                            follower
                        }
                        _stateFlow.value = RatingUI.Success(a)
                    }
                }
            }
        }
    }

    private fun fetchUsersMonthly() {
        launchIO {
            when (val wrapper = baseCloudRepository.getMonthlyChallenge()) {
                is ResultWrapper.Error -> {
                    Log.i(TAG, "fetchUsersMonthly: ERROR")
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                    makeToast(wrapper.error.toString())
                }
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        Log.i(TAG, "fetchUsersMonthly: Success")
                        val a = it.mapIndexed { index, follower ->
                            if (follower.user_id == userId) {
                                currentUser = follower
                            }
                            follower._id = index + 1
                            follower
                        }
                        _stateFlow.value = RatingUI.Success(a)
                    }
                }
            }
        }
    }



    fun retry() {
        fetchUsers()
    }

    fun refresh() {
        fetchUsers()
    }

    companion object{
        const val TAG = "ABO_ZHARYSTAR_LIST_VM"
    }

}