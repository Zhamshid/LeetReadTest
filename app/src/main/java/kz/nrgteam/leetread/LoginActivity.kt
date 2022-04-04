package kz.nrgteam.leetread

import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.databinding.ActivityLoginBinding
import kz.nrgteam.leetread.utils.base.BaseBindingActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    @Inject
    lateinit var prefs: Prefs

    override fun initViews(savedInstanceState: Bundle?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNavigationGraph()
    }

    private fun setNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.login_tab)
        navGraph.startDestination =
//            if (prefs.getBaseUrl().isEmpty()) {
                R.id.typeOfUsersFragment
//            } else {
//                R.id.login_fragment
//            }

        navController.graph = navGraph
    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//
//        // Check for user authentication
//        if(sharedViewModel.isUserAuthenticated()) {
//
//            (activity as MainActivity).makeHomeStart() //<---- THIS is the key
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.welcomeFragment, true)
//                .build()
//            navController.navigate(R.id.action_welcomeFragment_to_homeFragment,null,navOptions)
//
//        } else {
//            navController.navigate(R.id.action_welcomeFragment_to_loginFragment)
//        }
//    }

}