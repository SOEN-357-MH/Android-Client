package com.example.moviehub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.moviehub.databinding.FragmentLoginBinding
import com.example.moviehub.models.LoginBody
import com.example.moviehub.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(binding.root).navigate(action)

        }

        binding.loginButton.setOnClickListener{
            loginUser("thelilgrant", "123456")
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

//        binding.tinderBTN.setOnClickListener {
//            val action = LoginFragmentDirections.actionLoginFragmentToMovieTinderFragment()
//            Navigation.findNavController(binding.root).navigate(action)
//        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginUser(username: String, password: String){
        val loginBody = LoginBody(username, password)
        viewModel.loginUser(loginBody)
    }

    private fun observeLogin(){
        lifecycleScope.launchWhenStarted {
            viewModel.loginResponse.collect { event ->
                when(event){
                    is LoginViewModel.LoginEvent.Success -> Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                    is LoginViewModel.LoginEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is LoginViewModel.LoginEvent.Loading -> {

                    }
                    is LoginViewModel.LoginEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }
}