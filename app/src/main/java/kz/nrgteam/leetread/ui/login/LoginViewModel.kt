package kz.nrgteam.leetread.ui.login

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.model.auth.UserRequest
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    val baseCloudRepository: BaseCloudRepository,
    val prefs: Prefs
) : BaseViewModel() {
    private var _loginSuccess = MutableLiveData(false)
    val loginSuccess get() = _loginSuccess

    private var _errorTextMsg = MutableLiveData("")
    val errorTextMsg get() = _errorTextMsg

    private var _loading = MutableLiveData(false)
    val loading get() = _loading


    fun login(userRequest: UserRequest) {
        launchIO {
            _loading.postValue(true)
            if (userRequest.email.isEmpty() || userRequest.email.isBlank()) {
                errorTextMsg.postValue("Логин толтырыңыз")
                _loginSuccess.postValue(false)
                _loading.postValue(false)
                return@launchIO
            } else if (userRequest.password.isEmpty()) {
                errorTextMsg.postValue("Парольды толтырыңыз")
                _loginSuccess.postValue(false)
                _loading.postValue(false)
                return@launchIO
            } else if (!isValidEmail(userRequest.email)) {
                errorTextMsg.postValue("Логиныңыз қате жазылды")
                _loginSuccess.postValue(false)
                _loading.postValue(false)
                return@launchIO
            }

            when (val wrapper = baseCloudRepository.login(userRequest)) {
                is ResultWrapper.Success -> {
                    prefs.setAccessToken(wrapper.value.accessToken)
                    prefs.setUserId(wrapper.value.id)
                    prefs.setPassword(userRequest.password)
                    _loginSuccess.postValue(true)
                }
                is ResultWrapper.Error -> {
                    when (wrapper.code) {
                        500 -> {
                            errorTextMsg.postValue("Сервер өшулі, өтінемін кейінірек кіріңіз")
                        }
                        401 -> {
                            errorTextMsg.postValue("Логин немесе құпия сөз қате")
                        }
                        else -> {
                            errorTextMsg.postValue(wrapper.error)
                        }
                    }
                    _loginSuccess.postValue(false)
                    _loading.postValue(false)
                }
            }
            _loading.postValue(false)
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(target).matches()
    }

}