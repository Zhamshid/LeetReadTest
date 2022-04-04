package kz.nrgteam.leetread.ui.rating.ratingTable

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.InternalDeepLink
import kz.nrgteam.leetread.databinding.FragmentRatingTableBinding
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.ui.zharystarList.adapter.ReadersListAdapter
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RatingTableFragment() :
    BaseBindingFragment<FragmentRatingTableBinding>(R.layout.fragment_rating_table) {
    private var tabLayoutPosition: Int = 0

    constructor(position: Int) : this() {
        this.tabLayoutPosition = position
    }

    private lateinit var readersListAdapter: ReadersListAdapter
    private val viewModel: RatingTableViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        viewModel.tabLayoutPosition = tabLayoutPosition
        binding.setListeners()
        initAdapter()
        setObservers()
    }

    private fun FragmentRatingTableBinding.setListeners() {
        retryBtn.setOnClickListener {
            viewModel.retry()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

    }

    private fun initAdapter() {
        readersListAdapter = ReadersListAdapter(true, viewModel.userID, {
            onUserClicked(it)
        }, { user, pos ->
            Log.i(TAG, "initAdapter: $user")
            initCurrentUser(user, pos)
        })
        binding.ratingTableReadersRecyclerView.adapter = readersListAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun initCurrentUser(user: Follower, pos: Int) {
        implementOnScrollRecyclerView(user._id)
        binding.run {
            topUserPlace.text = "$pos."
            topUserNameAndSurname.text = user.first_name + " " + user.last_name
            topUserSchool.text = "Бітірген кітап саны : ${user.finished_book_count}"
            topUserScore.text = user.score.toString()
            userPlace.text = "${pos.plus(1)}."
            userNameAndSurname.text = user.first_name + " " + user.last_name
            userSchool.text = "Бітірген кітап саны : ${user.finished_book_count}"
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
        val navigation = (InternalDeepLink.PROFILE + "/${user.user_id}").toUri()
        findNavController().navigate(navigation)
    }

    private fun setObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    RatingUI.Loading -> {
                        loading()
                    }
                    RatingUI.NotLoading -> {
                        notLoading()
                    }
                    is RatingUI.Success -> {
                        usersGot(result.value)
                        notLoading()
                    }
                    is RatingUI.Error -> {
                        errorOccurred()
                    }
                    RatingUI.Default -> {}
                }
            }
        }
    }

    private fun errorOccurred() {
        binding.apply {
            swipeRefresh.visibility = View.GONE
            progressBar.hide()
            retryBtn.visibility = View.VISIBLE
            swipeRefresh.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun usersGot(list: List<Follower>) {
        readersListAdapter.setData(list)
    }

    private fun notLoading() {
        binding.apply {
            swipeRefresh.visibility = View.VISIBLE
            progressBar.hide()
            retryBtn.visibility = View.GONE
            swipeRefresh.isRefreshing = false
        }
    }

    private fun loading() {
        binding.apply {
            progressBar.show()
            swipeRefresh.visibility = View.GONE
            retryBtn.visibility = View.GONE
        }
    }

    private fun implementOnScrollRecyclerView(currentUserPosition: Int) {
        binding.ratingTableReadersRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val posLast =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val posFirst =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (posLast >= currentUserPosition) {
                    binding.userBottomLayout.visibility = View.INVISIBLE
                } else {
                    binding.userBottomLayout.visibility = View.VISIBLE
                }
                if (posFirst <= currentUserPosition) {
                    binding.topUserLayout.visibility = View.INVISIBLE
                } else {
                    binding.topUserLayout.visibility = View.VISIBLE
                }
            }
        })
    }

    companion object{
        const val TAG = "ABO_RATING_TABLE"
    }
}