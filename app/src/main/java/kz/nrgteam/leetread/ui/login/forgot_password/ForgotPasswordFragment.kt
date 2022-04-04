package kz.nrgteam.leetread.ui.login.forgot_password

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentForgotPasswordBinding
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseBindingFragment<FragmentForgotPasswordBinding>(R.layout.fragment_forgot_password) {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        binding.run {
            email.setText(viewModel.email)
            email.addTextChangedListener {
                viewModel.email = it.toString()
            }
            sendBtn.setOnClickListener {
                viewModel.sendNewPassword()
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest {
                binding.progressBar.isVisible = it is ForgotPasswordUI.Loading
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.toast.collectLatest {
                toast(requireContext(), it, true)
            }
        }
    }
}