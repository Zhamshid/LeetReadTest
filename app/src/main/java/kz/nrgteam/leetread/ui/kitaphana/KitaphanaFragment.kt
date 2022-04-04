package kz.nrgteam.leetread.ui.kitaphana

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentKitaphanaBinding
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.dto.kitaphana.Quote
import kz.nrgteam.leetread.ui.all_books.AllBooksOf
import kz.nrgteam.leetread.ui.kitaphana.adapters.ChildKitaptarAdapter
import kz.nrgteam.leetread.ui.kitaphana.adapters.ParentKitaptarAdapter
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import kz.nrgteam.leetread.utils.navigate
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class KitaphanaFragment :
    BaseBindingFragment<FragmentKitaphanaBinding>(R.layout.fragment_kitaphana),
    ChildKitaptarAdapter.KitaphanaAdapter {

    private lateinit var parentAdapter: ParentKitaptarAdapter
    private val viewModel: KitaphanaViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        parentAdapter = ParentKitaptarAdapter(this) {
            onClickedSeeMore()
        }
        initViews()
        setObservers()
        viewModel.refresh()
    }


    private fun setObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collect { result ->
                when (result) {
                    is KitaphanaUI.Error -> {
                        finishRefresh()
                        errorOccurred()
                    }
                    KitaphanaUI.Loading -> {
                        loading()
                    }
                    is KitaphanaUI.Success -> {
                        kitaptarGot(result.value)
                        finishRefresh()
                        notLoading()
                    }
                    KitaphanaUI.NotRefresh -> {
                        finishRefresh()
                    }
                }
            }
        }
    }

    private fun kitaptarGot(value: List<KitaphanaParentUI>) {
        parentAdapter.apply {
            items = value
        }
    }

    private fun finishRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    private fun notLoading() {
        binding.parentRecyclerView.visibility = View.VISIBLE
        binding.progressBar.hide()
        binding.retryBtn.visibility = View.GONE
    }

    private fun loading() {
        binding.parentRecyclerView.visibility = View.GONE
        binding.progressBar.show()
        binding.retryBtn.visibility = View.GONE
    }

    private fun errorOccurred() {
        binding.parentRecyclerView.visibility = View.GONE
        binding.progressBar.hide()
        binding.retryBtn.visibility = View.VISIBLE
    }

    private fun initViews() {
        binding.apply {
            parentRecyclerView.apply {
                adapter = parentAdapter
            }
            retryBtn.setOnClickListener {
                viewModel.refresh()
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.refresh()
            }

        }
    }

    private fun navigateToKitap(id: Int) {
        val action = KitaphanaFragmentDirections
            .actionKitaphanaFragmentToKitapFragment2(id)
        navigate(action)
    }

    override fun onClickKitap(kitap: Book) {
        kitap.id?.let { navigateToKitap(it) }

    }

    private fun openBook(book: Book) {
        val nav = KitaphanaFragmentDirections.actionKitaphanaFragmentToReaderFragment2(
            book
        )
        navigate(nav)
    }

    override fun onClickEditorChoice(id: String) {
        toast(requireContext(), "Әзірге дамуда, келесі нұсқаны күтіңіз")
    }

    override fun onClickQuote(quote: Quote) {
        openBook(
            Book(
                id = quote.book_id,
                epubFile = quote.epub_file,
                title = quote.title,
                pageCount = quote.page_count,
                lastPage = quote.cfi,
                page_count_progress = quote.page_count_progress ?: 0,
                _id = "",
                authors = null,
                bookCover = null,
                bookFile = null,
                coverFile = null,
                description = null,
                genre = null
            )
        )
    }

    private fun onClickedSeeMore() {
        val n = KitaphanaFragmentDirections.actionKitaphanaFragmentToAllBooksFragment(
            AllBooksOf.ALL_BOOKS
        )
        navigate(n)
    }
}