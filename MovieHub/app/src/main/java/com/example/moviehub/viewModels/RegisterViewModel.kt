package com.example.moviehub.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.helpers.DataStoreManager
import com.example.moviehub.helpers.NetworkHelper
import com.example.moviehub.models.UserBody
import com.example.moviehub.repository.MainRepository
import com.example.moviehub.utils.DispatcherProvider
import com.example.moviehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    sealed class RegisterEvent {
        class Success(val resultText: String): RegisterEvent()
        class Failure(val errorText: String): RegisterEvent()
        class Exception(val exceptionText: String): RegisterEvent()
        object Loading: RegisterEvent()
        object Empty: RegisterEvent()
    }

    private val _registerResponse = MutableStateFlow<RegisterEvent>(RegisterEvent.Empty)
    val registerResponse: StateFlow<RegisterEvent> = _registerResponse

    

    fun registerUser(userBody: UserBody) {
        viewModelScope.launch(dispatchers.io) {
            _registerResponse.value = RegisterEvent.Loading
            if(networkHelper.isNetworkConnected()) {
                when (val response = repository.registerUser(userBody)) {
                    is Resource.Error -> {
                        _registerResponse.value = RegisterEvent.Failure(response.message!!)
                    }
                    is Resource.Success -> {
                        _registerResponse.value = RegisterEvent.Success("Register Success!")
                    }
                    is Resource.Exception -> {
                        _registerResponse.value = RegisterEvent.Exception(response.message!!)
                    }
                }
            }else _registerResponse.value = RegisterEvent.Failure("No internet connection")
        }
    }
}