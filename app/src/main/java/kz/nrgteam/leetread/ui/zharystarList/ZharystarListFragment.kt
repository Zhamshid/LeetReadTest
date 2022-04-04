package kz.nrgteam.leetread.ui.zharystarList

import kz.nrgteam.leetread.MainActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.DividerItemDecorationWithoutFirstThree
import kz.nrgteam.leetread.databinding.FragmentZharystarListBinding
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.ui.rating.ratingTable.RatingUI
import kz.nrgteam.leetread.ui.zharystarList.adapter.ReadersListAdapter
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import kz.nrgteam.leetread.utils.navigate
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ZharystarListFragment :
    BaseBindingFragment<FragmentZharystarListBinding>(R.layout.fragment_zharystar_list) {
    private val viewModel: ZharystarListViewModel by viewModels()
    private lateinit var readersAdapter: ReadersListAdapter

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        binding.setListeners()
        setObservers()
    }

    private fun FragmentZharystarListBinding.setListeners() {
        retryBtn.setOnClickListener {
            viewModel.retry()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    RatingUI.Loading -> {
                        Log.i(TAG, "setObservers: loading")
                        binding.loading()
                    }
                    is RatingUI.Success -> {
                        Log.i(TAG, "setObservers: success")
                        usersGot(result.value)
                        binding.notLoading()
                    }
                    is RatingUI.Error -> {
                        Log.i(TAG, "setObservers: error occurred")
                        binding.errorOccurred()
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun usersGot(list: List<Follower>) {
        if (list.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
        } else {
            binding.empty.visibility = View.GONE
            implementOnScrollRecyclerView(viewModel.currentUser?._id ?: 0)
            readersAdapter.setData(list)
        }
    }

    private fun FragmentZharystarListBinding.errorOccurred() {
        swipeRefresh.visibility = View.GONE
        progressBar.hide()
        retryBtn.visibility = View.VISIBLE
        swipeRefresh.isRefreshing = false
    }

    private fun FragmentZharystarListBinding.notLoading() {
        swipeRefresh.isRefreshing = false
        swipeRefresh.visibility = View.VISIBLE
        progressBar.hide()
        retryBtn.visibility = View.GONE
    }

    private fun FragmentZharystarListBinding.loading() {
        progressBar.show()
        swipeRefresh.visibility = View.GONE
        retryBtn.visibility = View.GONE

    }

    private fun initView() {
        setToolbar()
        readersAdapter = ReadersListAdapter(false, viewModel.userId, {
            onUserClicked(it)
        }, { user, pos ->
            initCurrentUser(user, pos)
        })
        binding.readersRecyclerview.apply {
            adapter = readersAdapter
            addItemDecoration(getItemDecoration())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCurrentUser(user: Follower, pos: Int) {
        binding.apply {
            topUserPlace.text = pos.toString()
            topUserNameAndSurname.text = user.first_name + " " + user.last_name
            topUserSchool.text = user.school
            topUserScore.text = user.score.toString()
            userPlace.text = pos.toString()
            userNameAndSurname.text = user.first_name + " " + user.last_name
            userSchool.text = user.school
            userScore.text = user.score.toString()
            Glide.with(root.context)
                .load(user.cover_file)
                .into(topUserImage)
            Glide.with(root.context)
                .load(user.cover_file)
                .into(userImage)
        }
    }

    private fun onUserClicked(user: Follower) {
        val navigation =
            ZharystarListFragmentDirections.actionZharystarListFragmentToProfileFragment(user.user_id)
        navigate(navigation)
    }

    private fun setToolbar() {
        val activity = (activity as MainActivity)
        binding.apply {
            when (viewModel.zharystarTiming) {
                1 -> {
                    activity.setToolbarText(getString(R.string.daily))
                }
                2 -> {
                    activity.setToolbarText(getString(R.string.weekly))
                }
                3 -> {
                    activity.setToolbarText(getString(R.string.monthly))
                }
            }
        }
    }

    private fun getItemDecoration(): RecyclerView.ItemDecoration {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)
        return DividerItemDecorationWithoutFirstThree(drawable)
    }


    //On scroll recyclerview your place layout disappears.
    private fun implementOnScrollRecyclerView(currentUserPosition: Int) {
        binding.readersRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val posLast =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val posFirst =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (posLast >= currentUserPosition) {
                    binding.userLayout.visibility = View.INVISIBLE
                } else {
                    binding.userLayout.visibility = View.VISIBLE
                }
                if (posFirst <= currentUserPosition) {
                    binding.topUserLayout.visibility = View.INVISIBLE
                } else {
                    binding.topUserLayout.visibility = View.VISIBLE
                }
            }
        })
    }

    companion object {
        const val TAG = "ABO_ZHARYSTAR_LIST"
    }
}