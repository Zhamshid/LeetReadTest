package kz.nrgteam.leetread.ui.profile

import kz.nrgteam.leetread.MainActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.SpacingHorizontallyItemDecorator
import kz.nrgteam.leetread.databinding.FragmentProfileBinding
import kz.nrgteam.leetread.dto.user.Aim
import kz.nrgteam.leetread.dto.user.User
import kz.nrgteam.leetread.model.Zhetistik
import kz.nrgteam.leetread.ui.all_books.AllBooksOf
import kz.nrgteam.leetread.ui.profile.adapter.ZhetistikterAdapter
import kz.nrgteam.leetread.ui.zhetistikter.StatisticsAdapter
import kz.nrgteam.leetread.ui.zhetistikter.StatisticsViewHolder
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import kz.nrgteam.leetread.utils.navigate
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>(R.layout.fragment_profile),
    StatisticsViewHolder.StatisticsAdapterListener {
    private lateinit var statisticsAdapter: StatisticsAdapter
    private val viewModel: ProfileViewModel by viewModels()
    private val zhetistikterAdapter: ZhetistikterAdapter = ZhetistikterAdapter()

    override fun initViews(savedInstanceState: Bundle?) {
        viewModel.getProfile()
        checkIfThisMineProfile()
        setListeners()
        initZhetistikterRV()
        initStatisticsRV()
        initSwipeRefresher()
        observe()
    }

    private fun initStatisticsRV() {
        statisticsAdapter = StatisticsAdapter(this)
        binding.statisticsRv.adapter = statisticsAdapter
    }

    private fun checkIfThisMineProfile() {
        (activity as MainActivity).thisMyProfilePage(viewModel.isThisMyActualProfile)
//        binding.userSettingsBtn.isVisible = viewModel.isThisMyActualProfile
        binding.followBtn.isVisible = !viewModel.isThisMyActualProfile
    }

    private fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    ProfileUI.Loading -> {
                        loading()
                    }
                    ProfileUI.NotLoading -> {
                        notLoading()
                    }
                    is ProfileUI.Success -> {
                        notLoading()
                        userGot(result.value)
                    }
                    is ProfileUI.Error -> {
                        errorOccurred()
                    }
                    ProfileUI.Default -> {}
                }
            }
        }
        viewModel.statisticsLiveData.observe(viewLifecycleOwner) {
            statisticsAdapter.submitList(it)
        }
    }

    private fun subscribe() {
        if (binding.followBtn.text.toString() == getStr(R.string.subscribe)) {
            binding.followBtn.text = getStr(R.string.unsubscribe)
            binding.followersAmount.text =
                ((binding.followersAmount.text.toString().toInt().plus(1)).toString())
            viewModel.subscribe()
        } else {
            binding.followBtn.text = getStr(R.string.subscribe)
            binding.followersAmount.text =
                ((binding.followersAmount.text.toString().toInt().minus(1)).toString())
            viewModel.unsubscribe()
        }
    }

    private fun errorOccurred() {
        binding.apply {
            nestedScrollView.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryBtn.visibility = View.VISIBLE
        }
    }

    private fun userGot(user: User) {
        binding.user = user
        viewModel.currentUser = user
        setAnnualGoal(user.userInfo.aim)
        setFollowingBtnText()
        setAvatar()
        zhetistikterAdapter.submitList(user.userAchievements.map {
            Zhetistik(
                it.achievement,
                isFinished = 1 == it.status
            )
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setAnnualGoal(aim: Aim?) {
        val finished: Int
        val toFinish: Int
        val finishedPercentage: Int
        if (aim == null) {
            finished = 0
            toFinish = 0
            finishedPercentage = 0
        } else {
            finished = aim.finished ?: 0
            toFinish = aim.books_tofinish ?: 0
            finishedPercentage = try {
                (finished / toFinish.toDouble() * 100).toInt()
            } catch (e: Exception) {
                0
            }
        }
        binding.targetProgressBar.progress = finishedPercentage
        binding.targetFinishedTitle.text = "Осы жылғы мақсат $finished/$toFinish"
        binding.targetFinishedPercent.text = "$finishedPercentage%"
    }

    private fun notLoading() {
        binding.apply {
            progressBar.visibility = View.GONE
            retryBtn.visibility = View.GONE
            nestedScrollView.visibility = View.VISIBLE
            swipeContainer.isRefreshing = false
        }
    }

    private fun loading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            nestedScrollView.visibility = View.GONE
            retryBtn.visibility = View.GONE
        }
    }

    private fun setFollowingBtnText() {
        if (viewModel.currentUser?.userInfo?.is_follow != "0") {
            binding.followBtn.text = getString(R.string.unsubscribe)
        } else
            binding.followBtn.text = getString(R.string.subscribe)
    }

    private fun initSwipeRefresher() {
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun initZhetistikterRV() {
        binding.zhetistikterRecyclerView.run {
            adapter = zhetistikterAdapter
            addItemDecoration(getItemDecoration())
        }
    }

    private fun setAvatar() {
        Glide.with(requireActivity())
            .load(viewModel.currentUser?.userInfo?.cover_file)
            .error(R.drawable.ic_no_image)
            .into(binding.avatar)
    }

    private fun getItemDecoration(): RecyclerView.ItemDecoration {
        val horMargin =
            requireActivity().resources.getDimension(R.dimen.layout_zhetistikter_item_margin)
        return SpacingHorizontallyItemDecorator(horMargin.toInt())
    }

    private fun onClickTolygyrak() {
        val navigation = ProfileFragmentDirections.actionProfileFragmentToZhetistikterFragment()
        navigate(navigation)
    }

    private fun navigateToClickOquProgress() {
        val n = ProfileFragmentDirections.actionProfileFragmentToOquProgressFragment()
        navigate(n)
    }

    private fun onClickUserSettings() {
        val navigation = ProfileFragmentDirections.actionProfileFragmentToUserSettingsFragment(
            viewModel.currentUser?.userInfo?.toUserInfo()
        )
        navigate(navigation)
    }

    private fun onClickSettings() {
        val navigation = ProfileFragmentDirections.actionProfileFragmentToUserSettingsFragment(
            viewModel.currentUser?.userInfo?.toUserInfo()
        )
        navigate(navigation)
    }

    private fun onClickFollowers(isFollowers: Boolean) {
        val navigation = ProfileFragmentDirections.actionProfileFragmentToFollowersFragment(
            isFollowers,
            viewModel.userID
        )
        navigate(navigation)
    }

    private fun setListeners() {
        binding.run {
//            statisticsTolygyrakContainer.setOnClickListener {
//                onClickTolygyrak()
//            }
//            zhetistikterTolygyrakBtn.setOnClickListener {
//                onClickTolygyrak()
//            }
            userSettingsBtn.setOnClickListener {
                onClickUserSettings()
            }
            requireActivity().findViewById<ImageView>(R.id.settings)?.apply {
                setOnClickListener {
                    onClickSettings()
                }
            }
            retryBtn.setOnClickListener {
                viewModel.retry()
            }
            followersBtn.setOnClickListener {
                onClickFollowers(true)
            }
            followingsBtn.setOnClickListener {
                onClickFollowers(false)
            }
            followBtn.setOnClickListener {
                Log.i(TAG, "setListeners: followBtnClicked")
                subscribe()
            }
            annualReadTarget.setOnClickListener {
                navigateToMyFinishedBooks()
            }
        }
    }

    private fun navigateToMyFinishedBooks() {
        val nav = ProfileFragmentDirections.actionProfileFragmentToAllBooksFragment(
            AllBooksOf.FINISHED_BOOKS,
            viewModel.userID
        )
        navigate(nav)
    }

    override fun readDayNumberClicked() {
        navigateToClickOquProgress()
    }

    companion object {
        const val TAG = "ABO_PROFILE"
    }
}