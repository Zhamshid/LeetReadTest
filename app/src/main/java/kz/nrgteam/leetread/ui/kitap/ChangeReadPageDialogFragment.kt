package kz.nrgteam.leetread.ui.kitap

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.DialogChangeReadBinding
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChangeReadPageDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: ChangeReadPageViewModel by viewModels()
    private var _binding: DialogChangeReadBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangeReadBinding.inflate(inflater, container, false)
        collectData()
        return binding.root
    }

    private fun collectData() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest {
                when (it) {
                    is ChangeLastPageUI.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                    ChangeLastPageUI.NotLoading -> {

                    }
                    is ChangeLastPageUI.Success -> {
                        Toast.makeText(requireContext(), it.value, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            root.setBackgroundResource(R.drawable.radius_only_top)
            pageEditText.setText(viewModel.lastPageNumber.toString())
            pageEditText.addTextChangedListener {
                when {
//                    viewModel.allPageCounts < it.toString().toInt() -> {
//                        pageEditText.setText(it.toString())
//                    }
                    it.toString().contains("-") -> {
                        pageEditText.setText("0")
                    }
                    it.toString().isBlank() -> {
                        pageEditText.hint = "0"
                        viewModel.setLastPage("0")
                    }
                    else -> viewModel.setLastPage(it.toString())
                }
            }
            plusBtn.setOnClickListener {
                increaseValue()
            }
            minusBtn.setOnClickListener {
                pageEditText.setText(
                    (viewModel.lastPageNumber - 1).toString()
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
            doneBtn.setOnClickListener {
                viewModel.changeLastReadPage()
            }
        }
    }

    private fun DialogChangeReadBinding.increaseValue() {
        pageEditText.setText(
            (viewModel.lastPageNumber+1).toString()
        )
    }

    private fun DialogChangeReadBinding.decreaseValue() {
        pageEditText.setText(
            (viewModel.lastPageNumber-1).toString()
        )
    }
}