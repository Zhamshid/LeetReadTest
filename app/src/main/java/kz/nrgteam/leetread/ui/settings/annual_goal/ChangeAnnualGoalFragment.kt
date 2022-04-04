package kz.nrgteam.leetread.ui.settings.annual_goal

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentChangeAnnualGoalBinding
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
class ChangeAnnualGoalFragment :
    BaseBindingFragment<FragmentChangeAnnualGoalBinding>(R.layout.fragment_change_annual_goal) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        collectData()
        binding.setOnClickListeners()
    }

    private fun FragmentChangeAnnualGoalBinding.setOnClickListeners() {
        annualGoal.setText(viewModel.annualGoalNumber)
        annualGoal.addTextChangedListener {
            if (it.toString().contains("-")) {
                annualGoal.setText("0")
            }
            else viewModel.annualGoalNumber = it.toString()
        }
        requireActivity().findViewById<ImageView>(R.id.save)?.run {
            setOnClickListener {
                viewModel.updateAnnualGoal()
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

    private fun FragmentChangeAnnualGoalBinding.loading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun FragmentChangeAnnualGoalBinding.notLoading() {
        progressBar.visibility = View.GONE
    }

}