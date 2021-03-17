package com.example.moviehub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.moviehub.databinding.FragmentRegisterBinding
import com.example.moviehub.models.UserBody
import com.example.moviehub.viewModels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Register Fragment")
        registerObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        registerUser("John", "Baroudi", "Slayer4206", "john@gmail.co", "123456")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registerUser(firstName: String, lastName: String, username: String, email: String, password: String){
        val userBody = UserBody(firstName, lastName, username, email, password)
        viewModel.registerUser(userBody)
    }

    private fun registerObserver(){
        lifecycleScope.launchWhenStarted {
            viewModel.registerResponse.collect { event ->
                when(event){
                    is RegisterViewModel.RegisterEvent.Success -> Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                    is RegisterViewModel.RegisterEvent.Failure -> {
                        Timber.d(event.errorText)
                        Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    }
                    is RegisterViewModel.RegisterEvent.Loading -> {

                    }
                    is RegisterViewModel.RegisterEvent.Exception -> {
                        Timber.d(event.exceptionText)
                        Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}