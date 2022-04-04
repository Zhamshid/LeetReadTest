package kz.nrgteam.leetread.ui.login.forgot_password

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.core.util.PatternsCompat
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    val baseCloudRepository: BaseCloudRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var email: String? = savedStateHandle.get<String>("email")

    private val _stateFlow = MutableStateFlow<ForgotPasswordUI>(ForgotPasswordUI.NotLoading)
    val stateFlow = _stateFlow.asStateFlow()

    fun sendNewPassword() {
        launchIO {
            email?.let {
                if (isValidEmail(it)) {
                    _stateFlow.value = ForgotPasswordUI.Loading
                    when (val wrapper = baseCloudRepository.requestForNewPassword(it)) {
                        is ResultWrapper.Error -> {
                            _stateFlow.value = ForgotPasswordUI.NotLoading
                            if (wrapper.code == 204) {
                                makeToast("Почтаңызға құпия сөз жіберілді")
                            }
                            makeToast(wrapper.error.toString())
                        }
                        is ResultWrapper.Success -> {
                            _stateFlow.value = ForgotPasswordUI.NotLoading
                            makeToast("Почтаңызға құпия сөз жіберілді")
                        }
                    }
                } else {
                    makeToast("Сіздің жазғанызың логинге жатпайды.")
                }
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(target).matches()
    }

}

sealed class ForgotPasswordUI {
    object Loading : ForgotPasswordUI()
    object NotLoading : ForgotPasswordUI()
}