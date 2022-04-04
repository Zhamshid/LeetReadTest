package kz.nrgteam.leetread.ui.login

import kz.nrgteam.leetread.MainActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentLoginBinding
import kz.nrgteam.leetread.model.auth.UserRequest
import kz.nrgteam.leetread.utils.navigate
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setObservers()
        setOnClickListeners()
    }

    private fun setObservers() {
        viewModel.errorTextMsg.observe(viewLifecycleOwner) {
            binding.errorTextMsg.text = it
        }
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else{
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setOnClickListeners() {
        binding.enterButton.setOnClickListener {
            val userRequest = UserRequest(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
            viewModel.login(userRequest)
        }
        binding.forgotPassword.setOnClickListener {
            navigateToForgotPassword()
        }
    }

    private fun navigateToForgotPassword() {
        val n = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment(
            binding.emailEditText.text.toString()
        )
        navigate(n)
    }
}