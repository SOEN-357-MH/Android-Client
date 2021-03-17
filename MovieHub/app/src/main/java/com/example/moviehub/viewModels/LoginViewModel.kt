package com.example.moviehub.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.helpers.NetworkHelper
import com.example.moviehub.models.LoginBody
import com.example.moviehub.repository.MainRepository
import com.example.moviehub.utils.DispatcherProvider
import com.example.moviehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider,
    private val networkHelper: NetworkHelper
): ViewModel() {

    sealed class LoginEvent {
        class Success(val resultText: String): LoginEvent()
        class Failure(val errorText: String): LoginEvent()
        class Exception(val exceptionText: String): LoginEvent()
        object Loading: LoginEvent()
        object Empty: LoginEvent()
    }

    private val _loginResponse = MutableStateFlow<LoginEvent>(LoginEvent.Empty)
    val loginResponse: StateFlow<LoginEvent> = _loginResponse

    fun loginUser(loginBody: LoginBody){
        viewModelScope.launch(dispatchers.io){
            _loginResponse.value = LoginEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.loginUser(loginBody)){
                    is Resource.Success -> _loginResponse.value = LoginEvent.Success("Login Success")
                    is Resource.Error -> _loginResponse.value = LoginEvent.Failure(response.message!!)
                    is Resource.Exception -> _loginResponse.value = LoginEvent.Exception(response.message!!)
                }
            }else _loginResponse.value = LoginEvent.Failure("No internet connection")
        }
    }
}