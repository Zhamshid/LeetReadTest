package kz.nrgteam.leetread.ui.all_books

import kz.nrgteam.leetread.MainActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.InternalDeepLink
import kz.nrgteam.leetread.databinding.FragmentAllBooksBinding
import kz.nrgteam.leetread.dto.kitaphana.Kitap
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllBooksFragment : BaseBindingFragment<FragmentAllBooksBinding>(R.layout.fragment_all_books),
    AllBooksAdapter.AllBooksAdapterListener {

    private lateinit var allBooksAdapter: AllBooksAdapter
    private val viewModel: AllBooksViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        setToolbar(viewModel.allBooksType)
        allBooksAdapter = AllBooksAdapter(this)
        binding.allBooksRv.adapter = allBooksAdapter
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest {
                when (it) {
                    is AllBooksUI.Error -> {
                        binding.errorOccurred()
                    }
                    AllBooksUI.Loading -> binding.loading()
                    is AllBooksUI.Success -> {
                        gotAllBooks(it.users)
                        binding.notLoading()
                    }
                }
            }
        }
    }

    private fun setToolbar(allBooksType: AllBooksOf?) {
        val text = when(allBooksType){
            AllBooksOf.ALL_BOOKS -> "Барлық кітаптар"
            AllBooksOf.FINISHED_BOOKS -> "Менің кітаптарым"
            null -> ""
        }
        (activity as MainActivity).setToolbarText(text)
    }

    private fun gotAllBooks(users: List<Kitap>) {
        allBooksAdapter.submitList(users)
    }

    private fun FragmentAllBooksBinding.errorOccurred() {
        allBooksRv.visibility = View.GONE
        progressBar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
        empty.visibility = View.GONE
    }

    private fun FragmentAllBooksBinding.notLoading() {
        allBooksRv.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun FragmentAllBooksBinding.loading() {
        progressBar.visibility = View.VISIBLE
        allBooksRv.visibility = View.GONE
        retryButton.visibility = View.GONE
        empty.visibility = View.GONE
    }

    override fun onBookClicked(bookId: Int) {
        val navigation = (InternalDeepLink.KITAPHANA + "/${bookId}").toUri()
        findNavController().navigate(navigation)
    }
}