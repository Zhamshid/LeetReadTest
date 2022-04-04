package kz.nrgteam.leetread.ui.followers_list

import kz.nrgteam.leetread.MainActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentFollowersListBinding
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.ui.followers_list.adapter.FollowersAdapter
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import kz.nrgteam.leetread.utils.navigate
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FollowersFragment :
    BaseBindingFragment<FragmentFollowersListBinding>(R.layout.fragment_followers_list),
    FollowersAdapter.FollowersAdapter {

    private lateinit var followersAdapter: FollowersAdapter
    private val viewModel: FollowersViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        setObservers()
        if (viewModel.isFollowers) {
            (activity as MainActivity).setToolbarText(getString(R.string.followers))
        } else {
            (activity as MainActivity).setToolbarText(getString(R.string.followeds))
        }
    }

    private fun initView() {
        followersAdapter = FollowersAdapter(this)
        binding.followersListRcw.adapter = followersAdapter
    }


    private fun setObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    is FollowersUI.Error -> {
                        errorOccurred()
                    }
                    FollowersUI.Loading -> {
                        loading()
                    }
                    is FollowersUI.Success -> {
                        gotUsers(result.value)
                        notLoading()
                    }
                    FollowersUI.Default -> {}
                }

            }
        }
    }

    private fun gotUsers(value: List<Follower>) {
        if (value.isNotEmpty()) {
            followersAdapter.setData(value)
        }
    }

    private fun subscribe(pos: Int, id: Int) {
        viewModel.subscribe(id)
        followersAdapter.subscribedToThisFollower(pos)
    }

    private fun unsubscribe(pos: Int, id: Int) {
        viewModel.unsubscribe(id)
        followersAdapter.unsubscribedToThisFollower(pos)
    }


    private fun errorOccurred() {
        binding.apply {
            followersListRcw.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryBtn.visibility = View.VISIBLE
        }
    }

    private fun notLoading() {
        binding.apply {
            followersListRcw.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            retryBtn.visibility = View.GONE
        }
    }

    private fun loading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            followersListRcw.visibility = View.GONE
            retryBtn.visibility = View.GONE
        }
    }

    override fun onFollowClicked(position: Int, user: Follower) {
        if(user.user_id == viewModel.myActuallyID) return
        if (viewModel.isFollowers) {
            if (user.isInFollowing) {
                unsubscribe(position, user.user_id)
            } else subscribe(position, user.user_id)
        } else {
            if (user.isInFollowing) {
                unsubscribe(position, user.following_id)
            } else subscribe(position, user.following_id)
        }
    }

    override fun onFollowerClicked(user: Follower) {
        if (viewModel.isFollowers) {
            val navigation =
                FollowersFragmentDirections.actionFollowersFragmentToProfileFragment2(user.user_id)
            navigate(navigation)
        } else {
            val navigation =
                FollowersFragmentDirections.actionFollowersFragmentToProfileFragment2(user.following_id)
            navigate(navigation)
        }
    }

}
