package kz.nrgteam.leetread.splash

import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val prefs: Prefs
): BaseViewModel() {


    val accessToken = prefs.getAccessToken()
    val baseUrlType = prefs.getBaseUrl()
}