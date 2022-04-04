package kz.nrgteam.leetread

import kz.nrgteam.leetread.databinding.ActivityMainBinding
import kz.nrgteam.leetread.utils.base.BaseBindingActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.nrgteam.leetread.utils.setupWithNavController

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var currentNavController: LiveData<NavController>

    override fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.home_tab,
            R.navigation.zharystar_tab,
            R.navigation.kitaphana_tab,
            R.navigation.rating_tab,
            R.navigation.profile_tab
        )
        val controller = binding.navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller

        controller.observe(this) { navController ->
            setupActionBarWithNavController(navController)
            currentNavController.value?.addOnDestinationChangedListener(listener)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value!!.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    private val listener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            when (destination.id) {
                R.id.fragment_home -> {
                    binding.toolbarTitle.text = getString(R.string.home_page_title)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.kitaphana_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.kitaphana)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.kitap_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.kitap_title)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.zharystar_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.zharystar)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.rating_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.rating)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.profile_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.zheke_profile)
                    binding.settings.visibility = View.VISIBLE
                    binding.heartImage.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.zhetistikter_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.zhetistikter)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.user_settings_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.user_settings)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.changePasswordFragment -> {
                    binding.toolbarTitle.text = getString(R.string.change_password)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.VISIBLE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_cancel)
                }
                R.id.settings_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.settings)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.changeAnnualGoalFragment -> {
                    binding.toolbarTitle.text = getString(R.string.change_annual_goal)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.VISIBLE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_cancel)
                }
                R.id.oqu_progress_fragment -> {
                    binding.toolbarTitle.text = getString(R.string.oqu_progress)
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
                R.id.reader_fragment -> {
                    setVisibleToolbarAndBottomNav(false)
                }
                else -> {
                    binding.heartImage.visibility = View.GONE
                    binding.settings.visibility = View.GONE
                    binding.save.visibility = View.GONE
                }
            }
            if (destination.id == R.id.reader_fragment) {
                setVisibleToolbarAndBottomNav(false)
            } else {
                setVisibleToolbarAndBottomNav(true)
            }
        }

    fun setToolbarText(text: String) {
        binding.toolbarTitle.text = text
    }

    fun changeBackButtonToCancel(){
        binding.toolbar.setNavigationIcon(R.drawable.ic_cancel)
    }
    fun changeBackButtonToArrow(){
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
    }

    private fun setVisibleToolbarAndBottomNav(isVisible: Boolean) {
        binding.toolbar.isVisible = isVisible
        binding.navView.isVisible = isVisible
    }

    fun thisMyProfilePage(isIt: Boolean) {
        if (isIt) {
            binding.heartImage.visibility = View.INVISIBLE
            binding.toolbarTitle.text = getString(R.string.zheke_profile)
        } else {
            binding.heartImage.visibility = View.GONE
            binding.settings.visibility = View.GONE
            binding.toolbarTitle.text = getString(R.string.profile)
        }
    }
}