package com.example.moviehub.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.helpers.NetworkHelper
import com.example.moviehub.repository.MainRepository
import com.example.moviehub.repository.MediaRepository
import com.example.moviehub.utils.DispatcherProvider
import com.example.moviehub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val dispatchers: DispatcherProvider,
    private val networkHelper: NetworkHelper
): ViewModel() {

    sealed class GetTrendingMoviesByPageEvent {
        class Success(val resultText: String): GetTrendingMoviesByPageEvent()
        class Failure(val errorText: String): GetTrendingMoviesByPageEvent()
        class Exception(val exceptionText: String): GetTrendingMoviesByPageEvent()
        object Loading: GetTrendingMoviesByPageEvent()
        object Empty: GetTrendingMoviesByPageEvent()
    }

    private val _getTrendingMoviesByPageResponse = MutableStateFlow<GetTrendingMoviesByPageEvent>(GetTrendingMoviesByPageEvent.Empty)
    val getTrendingMoviesByPageResponse: StateFlow<GetTrendingMoviesByPageEvent> = _getTrendingMoviesByPageResponse

    fun getTrendingMoviesByPage(page: Int){
        viewModelScope.launch(dispatchers.io){
            _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getTrendingMoviesByPage(page)){
                    is Resource.Success -> _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Success("Trending Movies Retrieved")
                    is Resource.Error -> _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Failure(response.message!!)
                    is Resource.Exception -> _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Exception(response.message!!)
                }
            }else _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Failure("No internet connection")
        }
    }
}