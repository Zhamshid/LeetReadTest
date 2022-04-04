package kz.nrgteam.leetread.ui.settings

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.model.UserInfo
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: Prefs,
    private val repository: BaseCloudRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var wasChanges: Boolean = false
    private val userInfo: UserInfo? = savedStateHandle["user_info"]
    private val currentGoal: Int = savedStateHandle["current_goal"] ?: -1

    var userImage = userInfo?.imageUrl
    var userAnnualGoalNumber = userInfo?.annualGoalNumber ?: -1
    var imageBitmap: Bitmap? = null

    //change password page
    var currentPassword: String = ""
    var newPassword: String = ""
    var promptPassword: String = ""

    //change annual goal page
    var annualGoalNumber = currentGoal.toString()

    private val _stateFlow = MutableStateFlow<SettingsUI>(SettingsUI.NotLoading)
    val stateFlow = _stateFlow.asStateFlow()

    fun logout() {
        launchIO {
            prefs.setAccessToken("")
            prefs.setBaseUrl("")
        }
    }

    fun updateUserInfo(image: MultipartBody.Part) {
        launchIO {
            if (imageBitmap == null) {
                return@launchIO
            }
            _stateFlow.value = SettingsUI.Loading
            when (val wrapper = repository.updateUserImage(image)) {
                is ResultWrapper.Error -> {
                    makeToast("Қателік орнады. Басынан немесе кейінірек қайталап көріңіз")
                    Log.i(TAG, "applyChanges: ${wrapper.error}")
                    _stateFlow.value = SettingsUI.NotLoading
                }
                is ResultWrapper.Success -> {
                    Log.i(TAG, "applyChanges: ${wrapper.value}")
                    _stateFlow.value = SettingsUI.Success
                }
            }
        }
    }

    fun updatePassword() {
        launchIO {
            if (!passwordValidate()) {
                return@launchIO
            }
            _stateFlow.value = SettingsUI.Loading
            when (val wrapper = repository.updateUserPassword(newPassword)) {
                is ResultWrapper.Error -> {
                    makeToast("Қателік орнады. Басынан немесе кейінірек қайталап көріңіз")
                    Log.i(TAG, "updatePassword: ${wrapper.error}")
                    _stateFlow.value = SettingsUI.NotLoading
                }
                is ResultWrapper.Success -> {
                    Log.i(TAG, "updatePassword: ${wrapper.value}")
                    _stateFlow.value = SettingsUI.Success
                }

            }
        }
    }

    fun updateAnnualGoal() {
        launchIO {
            val goalNum = try {
                annualGoalNumber.trim().toInt()
            } catch (e: Exception) {
                makeToast("Сан жазыңыз, сөздің орнына")
                return@launchIO
            }
            _stateFlow.value = SettingsUI.Loading
            val wrapper = if (currentGoal == -1) {
                repository.setAnnualGoal(goalNum)
            } else repository.updateAnnualGoal(goalNum)
            when (wrapper) {
                is ResultWrapper.Error -> {
                    makeToast("Қателік орнады. Басынан немесе кейінірек қайталап көріңіз")
                    Log.i(TAG, "updatePassword: ${wrapper.error}")
                    _stateFlow.value = SettingsUI.NotLoading
                }
                is ResultWrapper.Success -> {
                    Log.i(TAG, "updatePassword: ${wrapper.value}")
                    _stateFlow.value = SettingsUI.Success
                }

            }
        }
    }

    private fun passwordValidate(): Boolean {
        if (newPassword.isEmpty() && promptPassword.isEmpty()) {
            makeToast("Жаңа құпия сөздер бос болмауы керек")
            return false
        }
        if (newPassword != promptPassword) {
            makeToast("Жаңа құпия сөздер сәйкес келмейді")
            return false
        }
        if (newPassword.length < 6) {
            makeToast("Жаңа құпия сөз ұзындығы 6 сөз немесе оданда көп болуы тиіс")
            return false
        }
        if (currentPassword != prefs.getPassword()) {
            makeToast("Қазіргі құпия сөзіңіз қате")
            return false
        }
        return true
    }

    companion object {
        const val TAG = "ABO_SETTINGS_VM"
    }
}

sealed class SettingsUI {
    object NotLoading : SettingsUI()
    object Loading : SettingsUI()
    object Success : SettingsUI()
}