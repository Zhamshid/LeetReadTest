package kz.nrgteam.leetread.ui.rating.ratingTable

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.dto.user.User
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RatingTableViewModel @Inject constructor(
    val baseCloudRepository: BaseCloudRepository,
    pref: Prefs
) :
    BaseViewModel() {
    var tabLayoutPosition: Int = 0
    val userID = pref.getUserId()
    var currentUser: User? = null
    var myPos = 0
    private val _stateFlow = MutableStateFlow<RatingUI>(RatingUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        _stateFlow.value = RatingUI.Loading
        getUser()
    }

    private fun getUser() {
        launchIO {
            when (val wrapper = baseCloudRepository.getProfile(userID)) {
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        currentUser = it
                        defineTabGetUsers()
                    }
                }
                is ResultWrapper.Error -> {
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                    Log.i("fail", wrapper.error.toString())
                    makeToast(wrapper.error.toString())
                }
            }
        }
    }

    private fun defineTabGetUsers() {
        when (tabLayoutPosition) {
            0 -> {
                getUsersGrade()
            }
            1 -> {
                getUsersSchool()
            }
            2 -> {
                getUsersCountry()
            }
        }
    }

    private fun getUsersSchool() {
        launchIO {
            Log.i(TAG, "getUsersSchool: "+currentUser?.userInfo?.school_id)
            when (val wrapper =
                baseCloudRepository.getSchoolRating(currentUser?.userInfo?.school_id ?: 0)) {
                is ResultWrapper.Error -> {
                    makeToast(wrapper.error.toString())
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        _stateFlow.value = RatingUI.Success(it.mapIndexed { index, follower ->
                            if (follower.user_id == userID) myPos = index + 1
                            follower._id = index + 1
                            follower
                        })
                    }
                }
            }
        }
    }

    private fun getUsersGrade() {
        launchIO {
            when (val wrapper =
                baseCloudRepository.getClassRating(
                    currentUser?.userInfo?.school_id ?: -1,
                    currentUser?.userInfo?.grade ?: -1
                )) {
                is ResultWrapper.Error -> {
                    makeToast(wrapper.error.toString())
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        _stateFlow.value = RatingUI.Success(it.mapIndexed { index, follower ->
                            if (follower.user_id == userID) myPos = index + 1
                            follower._id = index + 1
                            follower
                        })
                    }
                }
            }
        }
    }

    private fun getUsersCountry() {
        launchIO {
            when (val wrapper = baseCloudRepository.getCountryRating()) {
                is ResultWrapper.Error -> {
                    makeToast(wrapper.error.toString())
                    _stateFlow.value = RatingUI.Error(wrapper.error)
                }
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        _stateFlow.value = RatingUI.Success(it.mapIndexed { index, follower ->
                            if (follower.user_id == userID) myPos = index + 1
                            follower._id = index + 1
                            follower
                        })
                    }
                }
            }
        }
    }

    fun retry() {
        _stateFlow.value = RatingUI.Loading
        getUser()
    }

    fun refresh() {
        _stateFlow.value = RatingUI.Default
        getUser()
    }

    companion object{
        const val TAG = "ABO_RATING_TABLE_VM"
    }

}

sealed class RatingUI {
    object Loading : RatingUI()
    object NotLoading : RatingUI()
    object Default : RatingUI()
    data class Success(val value: List<Follower>) : RatingUI()
    class Error(val error: String?) : RatingUI()
}