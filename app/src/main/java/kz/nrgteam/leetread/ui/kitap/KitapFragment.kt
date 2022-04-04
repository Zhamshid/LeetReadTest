package kz.nrgteam.leetread.ui.kitap

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentKitapBinding
import kz.nrgteam.leetread.dto.kitaphana.BookItem
import kz.nrgteam.leetread.ui.zharystar.adapter.UserReaderAdapter
import kz.nrgteam.leetread.utils.navigate
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class KitapFragment : Fragment(R.layout.fragment_kitap) {

    private val viewModel: KitapViewModel by viewModels()
    lateinit var binding: FragmentKitapBinding
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val userDailyReaderAdapter = UserReaderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentKitapBinding.bind(view)
        binding.initCounter()
        binding.retryBtn.setOnClickListener { viewModel.retry() }
        setObservers()
        collectData()
        viewModel.fetchBook()
    }

    private fun FragmentKitapBinding.initCounter() {
        pageEditText.setText(viewModel.bookLastPage.toString())
        pageEditText.addTextChangedListener {
            when {
                it.toString().contains("-") -> {
                    pageEditText.setText("0")
                }
                it.toString().isBlank() || it.toString().isEmpty() -> {
                    pageEditText.hint = "0"
                    viewModel.bookLastPage = 0
                }
                viewModel.book?.book?.pageCount ?: -1 < it.toString().toInt() -> {
                    pageEditText.setText((viewModel.book?.book?.pageCount ?: 0 - 1).toString())
                }
                else -> viewModel.setLastPage(it.toString())
            }
        }
        plusBtn.setOnClickListener {
            increaseValue()
        }
        minusBtn.setOnClickListener {
            pageEditText.setText(
                (viewModel.bookLastPage - 1).toString()
            ).toString()
        }

        binding.plusBtn.setOnLongClickListener {
            runnable = Runnable {
                if (!binding.plusBtn.isPressed) return@Runnable
                increaseValue()
                runnable?.let { it1 -> handler.postDelayed(it1, 50) }
            }
            handler.postDelayed(runnable!!, 50)
            true
        }
        binding.minusBtn.setOnLongClickListener {
            runnable = Runnable {
                if (!binding.minusBtn.isPressed) return@Runnable
                decreaseValue()
                runnable?.let { it1 -> handler.postDelayed(it1, 50) }
            }
            handler.postDelayed(runnable!!, 50)
            true
        }
    }

    private fun FragmentKitapBinding.increaseValue() {
        pageEditText.setText(
            (viewModel.bookLastPage.plus(1)).toString()
        )
    }

    private fun FragmentKitapBinding.decreaseValue() {
        pageEditText.setText(
            (viewModel.bookLastPage.minus(1)).toString()
        )
    }

    private fun collectData() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    KitapUI.Loading -> {
                        loading()
                    }
                    KitapUI.NotLoading -> {
                        notLoading()
                    }
                    is KitapUI.Success -> {
                        gotBook(result.value)
                        notLoading()
                    }
                    is KitapUI.Error -> {
                        notLoading()
                        errorOccurred()
                    }
                }
            }
        }
    }


    private fun errorOccurred() {
        binding.apply {
            nestedScrollView.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryBtn.visibility = View.VISIBLE
        }
    }

    private fun gotBook(book: BookItem) {
        val lastPage = book.libraryInfo?.page_count_progress ?: 0
        initReadProgress(book.book.pageCount, lastPage)
        binding.pageEditText.setText(lastPage.toString())
        initView(book)
    }

    private fun initReadProgress(pageCount: Int?, pageCountProgress: Int) {
        val myProgress =
            (pageCountProgress.toFloat() / (pageCount ?: 1))
        binding.horProgressBar.run {
            progress = (myProgress * 100).toInt()
        }
    }

    private fun notLoading() {
        binding.run {
            nestedScrollView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            retryBtn.visibility = View.GONE
            swipeRefresh.isRefreshing = false
        }
    }

    private fun loading() {
        binding.run {
            progressBar.visibility = View.VISIBLE
            nestedScrollView.visibility = View.GONE
            retryBtn.visibility = View.GONE
        }
    }


    private fun setObservers() {
        viewModel.run {
            usersDaily.observe(viewLifecycleOwner) {
                userDailyReaderAdapter.submitList(it)
            }
        }
    }

    private fun initView(book: BookItem) {
        setOnClickListeners()
        binding.run {
            if (book.book.epubFile.isNullOrBlank()) {
                readBtn.alpha = 0.5f
            } else {
                readBtn.alpha = 1f
            }
            kitap = book.book
            bookDescription.text = book.book.description
            readersRecyclerviewDaily.adapter = userDailyReaderAdapter
            progressBar.progress = (10..100).random()
            Glide
                .with(requireContext())
                .load(book.book.coverFile)
                .error(R.drawable.ic_no_image)
                .into(bookImage)
        }
    }

    private fun setOnClickListeners() {
        binding.run {
            readBtn.setOnClickListener {
                if (viewModel.book?.book?.epubFile?.isNotEmpty() == true) {
                    val lastPage = try {
                        viewModel.book?.libraryInfo?.page_count_progress ?: 0
                    } catch (e: Exception) {
                        0
                    }
                    val action = KitapFragmentDirections.toReaderPage(
                        viewModel.book?.book!!.copy(
                            page_count_progress = lastPage,
                            lastPage = viewModel.book?.libraryInfo?.last_page ?: "0"
                        )
                    )
                    navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Бұл кітап жүйеге енгізілмеген",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onPause() {
        viewModel.changeLastReadPage()
        super.onPause()
    }
}