package kz.nrgteam.leetread.ui.settings.password

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentChangePasswordBinding
import kz.nrgteam.leetread.ui.settings.SettingsUI
import kz.nrgteam.leetread.ui.settings.SettingsViewModel
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseBindingFragment<FragmentChangePasswordBinding>(R.layout.fragment_change_password) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        collectData()
        binding.setOnClickListeners()
    }

    private fun FragmentChangePasswordBinding.setOnClickListeners() {
        currentPassword.setText(viewModel.currentPassword)
        currentPassword.addTextChangedListener {
            viewModel.currentPassword = it.toString()
        }
        newPassword.setText(viewModel.newPassword)
        newPassword.addTextChangedListener {
            viewModel.newPassword = it.toString()
        }
        prompPassword.setText(viewModel.newPassword)
        prompPassword.addTextChangedListener {
            viewModel.promptPassword = it.toString()
        }
        requireActivity().findViewById<ImageView>(R.id.save)?.run {
            setOnClickListener {
                viewModel.updatePassword()
            }
        }
    }

    private fun collectData() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    SettingsUI.Loading -> {
                        binding.loading()
                    }
                    SettingsUI.NotLoading -> {
                        binding.notLoading()
                    }
                    SettingsUI.Success -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.toast.collect {
                toast(requireContext(), it)
            }
        }
    }

    private fun FragmentChangePasswordBinding.loading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun FragmentChangePasswordBinding.notLoading() {
        progressBar.visibility = View.GONE
    }

}