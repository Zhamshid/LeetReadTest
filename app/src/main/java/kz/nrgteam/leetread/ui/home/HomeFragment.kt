package kz.nrgteam.leetread.ui.home

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.InternalDeepLink
import kz.nrgteam.leetread.databinding.FragmentHomeBinding
import kz.nrgteam.leetread.dto.user.UserOnline
import kz.nrgteam.leetread.ui.home.adapters.NewsAdapter
import kz.nrgteam.leetread.ui.home.adapters.OnlineViewHolder
import kz.nrgteam.leetread.ui.home.adapters.StoriesContainerAdapter
import kz.nrgteam.leetread.ui.home.adapters.UsersAdapter
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kz.nrgteam.leetread.ui.home.paging.LoadStateAdapter
import kz.nrgteam.leetread.utils.Constants

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home),
    NewsAdapter.HomeAdapterListener, OnlineViewHolder.UserAdapterListener {

    private val newsAdapter: NewsAdapter = NewsAdapter(this)
    private lateinit var onlineUsersContainer: StoriesContainerAdapter
    private lateinit var onlineUsersAdapter: UsersAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        binding.initUI()
        collectData()
    }

    private fun FragmentHomeBinding.initUI() {
        setClickListeners()
        initRecyclerView()
    }

    private fun FragmentHomeBinding.setClickListeners() {
        retryButton.setOnClickListener {
            newsAdapter.retry()
            viewModel.retry()
        }
        swipeRefresh.setOnRefreshListener {
            newsAdapter.refresh()
            viewModel.refresh()
        }
    }

    private fun FragmentHomeBinding.initRecyclerView() {
        onlineUsersAdapter = UsersAdapter(
            this@HomeFragment, Constants.USERS_ONLINE_LIST_HOR_MARGIN,
            Constants.USERS_ONLINE_LIST_ITEM_SIZE
        )
        onlineUsersContainer = StoriesContainerAdapter("Достарым", onlineUsersAdapter)
        homePageRecyclerView.adapter = ConcatAdapter(
            onlineUsersContainer,
            newsAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { newsAdapter.retry() }

            )
        )
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getNews().collect {
                newsAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            newsAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && newsAdapter.itemCount == 0
                binding.empty.isVisible = isListEmpty
                binding.homePageRecyclerView.isVisible = !isListEmpty
                if (loadState.source.refresh is LoadState.Loading) {
                    binding.loading()
                } else {
                    binding.notLoading()
                }

                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 ${"Оппа"} ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    NewsUI.Loading -> {
                        Log.i(TAG, "inStateFlow: Loading")
                        binding.loading()
                    }
                    is NewsUI.Success -> {
                        Log.i(TAG, "inStateFlow: got news and online users list")
                        gotNewsAndOnlineUsers(result.users)
                        binding.notLoading()
                    }
                    is NewsUI.Error -> {
                        Log.i(TAG, "inStateFlow: Error")
//                        binding.errorOccurred()
                    }
                }
            }
        }
    }

    private fun gotNewsAndOnlineUsers(users: List<UserOnline>) {
        onlineUsersAdapter.submitList(users)
    }

    private fun FragmentHomeBinding.errorOccurred() {
        homePageRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
        swipeRefresh.isRefreshing = false
        empty.visibility = View.GONE
    }

    private fun FragmentHomeBinding.notLoading() {
        homePageRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        retryButton.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

    private fun FragmentHomeBinding.loading() {
        progressBar.visibility = View.VISIBLE
        homePageRecyclerView.visibility = View.GONE
        retryButton.visibility = View.GONE
        empty.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

    override fun onShareClicked(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }

    override fun onUserClicked(id: Int) {
        val navigation = (InternalDeepLink.PROFILE + "/${id}").toUri()
        findNavController().navigate(navigation)
    }

    override fun onBookClicked(id: Int) {
        val navigation = (InternalDeepLink.KITAPHANA + "/${id}").toUri()
        findNavController().navigate(navigation)
    }

    companion object {
        const val TAG = "ABO_HOME_TAG"
    }

}