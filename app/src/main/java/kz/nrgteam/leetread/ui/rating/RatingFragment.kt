package kz.nrgteam.leetread.ui.rating

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.InternalDeepLink
import kz.nrgteam.leetread.databinding.FragmentRatingBinding
import kz.nrgteam.leetread.ui.home.paging.LoadStateAdapter
import kz.nrgteam.leetread.ui.rating.adapter.RatingTabLayoutAdapter
import kz.nrgteam.leetread.ui.search_user.SearchedUsersAdapter
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class RatingFragment : BaseBindingFragment<FragmentRatingBinding>(R.layout.fragment_rating) {

    private lateinit var ratingTabLayoutAdapter: RatingTabLayoutAdapter
    private val viewModel: RatingViewModel by viewModels()
    private lateinit var readersListAdapter: SearchedUsersAdapter
    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        binding.listeners()
        collectData()
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.pagingDataFlow.collect {
                readersListAdapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            readersListAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && readersListAdapter.itemCount == 0
                binding.empty.isVisible = isListEmpty
//                if (loadState.source.refresh is LoadState.Loading) {
//                    binding.loading()
//                } else {
//                    binding.notLoading()
//                }

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
    }

    private fun initView() {
        if (binding.searchEditTxt.text.toString().isBlank()) {
            binding.notSearching()
        } else {
            binding.searching()
        }
        readersListAdapter = SearchedUsersAdapter{
            onUserClicked(it)
        }
        ratingTabLayoutAdapter = RatingTabLayoutAdapter(requireActivity())
        binding.run {
            recyclerForSearch.adapter = readersListAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { readersListAdapter.retry() }
            )
            pager.adapter = ratingTabLayoutAdapter
            pager.offscreenPageLimit = 1
        }

        TabLayoutMediator(
            binding.tabLayout, binding.pager
        ) { tab, position ->
            tab.text = getString(RatingTabLayoutAdapter.TAB_TITLES[position])
        }.attach()
    }

    private fun onUserClicked(id: Int) {
        val navigation = (InternalDeepLink.PROFILE + "/${id}").toUri()
        findNavController().navigate(navigation)
    }

    private fun FragmentRatingBinding.listeners() {
        searchEditTxt.doOnTextChanged { text, _, _, after ->
            Log.i(TAG, "listeners: $text=$after")
            viewModel.searchedText = text.toString()
            if (after == 0) {
                notSearching()
            } else {
                searching()
            }
        }
        searchEditTxt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.accept(v.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        retryButton.setOnClickListener {
            viewModel.accept(viewModel.searchedText)
        }
    }


    private fun FragmentRatingBinding.searching() {
//        if (pager.isVisible) {
        pager.visibility = View.GONE
        empty.visibility = View.GONE
        retryButton.visibility = View.GONE
        recyclerForSearch.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
//        }
    }

    private fun FragmentRatingBinding.notSearching() {
        pager.visibility = View.VISIBLE
        recyclerForSearch.visibility = View.GONE
        empty.visibility = View.GONE
        retryButton.visibility = View.GONE
        progressBar.visibility = View.GONE

    }

    private fun FragmentRatingBinding.errorOccurred() {
        recyclerForSearch.visibility = View.GONE
        progressBar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
        empty.visibility = View.GONE
    }

//    private fun FragmentRatingBinding.notLoading() {
//        if (!pager.isVisible) {
//            recyclerForSearch.visibility = View.VISIBLE
//            progressBar.visibility = View.GONE
//            retryButton.visibility = View.GONE
//        }
//    }

    private fun FragmentRatingBinding.loading() {
        progressBar.visibility = View.VISIBLE
        recyclerForSearch.visibility = View.GONE
        retryButton.visibility = View.GONE
        empty.visibility = View.GONE
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.pager.isVisible) {
                isEnabled = false
                requireActivity().onBackPressed()
            } else {
                isEnabled = false
                binding.recyclerForSearch.isVisible = false
                binding.pager.isVisible = true
                binding.searchEditTxt.setText(" ")
            }
        }
    }

    companion object {
        const val TAG = "ABO_RATING_FRAGMENT"
    }

}