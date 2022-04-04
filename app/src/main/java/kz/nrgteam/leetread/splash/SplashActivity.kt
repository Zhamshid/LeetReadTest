package kz.nrgteam.leetread.splash

import kz.nrgteam.leetread.LoginActivity
import kz.nrgteam.leetread.MainActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
//        Handler().postDelayed({

            if (viewModel.accessToken.isEmpty() || viewModel.baseUrlType.isEmpty()){
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()


//        }, 400)
    }

}