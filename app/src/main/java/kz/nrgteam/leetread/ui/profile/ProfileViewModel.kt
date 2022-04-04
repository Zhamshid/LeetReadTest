package kz.nrgteam.leetread.ui.profile

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.user.User
import kz.nrgteam.leetread.model.Statistic
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val baseCloudRepository: BaseCloudRepository,
    val prefs: Prefs,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    companion object {
        const val TAG = "ABO_PROFILE_VM"
    }

    val myActuallyID = prefs.getUserId()
    val userID: Int = savedStateHandle["userID"] ?: myActuallyID
    var currentUser: User? = null
    val isThisMyActualProfile = myActuallyID == userID
    val thisYearGoal = Statistic(
        id = "0",
        title = "Осы жылғы мақсат",
        description = "100 кітап",
        goal = 10,
        currentAchievements = 5
    )

    private val _stateFlow = MutableStateFlow<ProfileUI>(ProfileUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        _stateFlow.value = ProfileUI.Loading
    }

    fun getProfile() {
        launchIO {
            when (val wrapper = baseCloudRepository.getProfile(userID)) {
                is ResultWrapper.Success -> {
                    wrapper.value.let {
                        _stateFlow.value = ProfileUI.Success(it)
                        statisticsLiveData.postValue(
                            listOf(
                                thisYearGoal.copy(
                                    description = (it.userInfo.aim?.books_tofinish
                                        ?: 0).toString() + " кітап",
                                    goal = it.userInfo.aim?.books_tofinish
                                        ?: 0,
                                    currentAchievements = it.userInfo.aim?.finished
                                        ?: 0
                                )
                            )
                        )
                    }
                }
                is ResultWrapper.Error -> {
                    _stateFlow.value = ProfileUI.Error(wrapper.error)
                    Log.e(TAG, "getProfile: ERROR")
                    makeToast(wrapper.error.toString())
                }
            }
        }
    }

    fun subscribe() {
        launchIO {
            when (baseCloudRepository.follow(userID)) {
                is ResultWrapper.Error -> makeToast("Error while subscribing")
                is ResultWrapper.Success -> {}
            }
        }
    }

    fun unsubscribe() {
        launchIO {
            when (baseCloudRepository.unFollow(userID)) {
                is ResultWrapper.Error -> makeToast("Error while unsubscribing")
                is ResultWrapper.Success -> {}
            }
        }
    }

    fun refresh() {
        _stateFlow.value = ProfileUI.Default
        getProfile()
    }

    fun retry() {
        _stateFlow.value = ProfileUI.Loading
        getProfile()
    }

    private val statistics = listOf(
        thisYearGoal
    )
    val statisticsLiveData = MutableLiveData<List<Statistic>>()
}


sealed class ProfileUI {
    object Loading : ProfileUI()
    object NotLoading : ProfileUI()
    object Default : ProfileUI()
    data class Success(val value: User) : ProfileUI()
    class Error(val error: String?) : ProfileUI()
}