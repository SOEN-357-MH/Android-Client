package com.example.moviehub.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.helpers.NetworkHelper
import com.example.moviehub.models.GenreModel
import com.example.moviehub.models.MediaBody
import com.example.moviehub.repository.MainRepository
import com.example.moviehub.repository.MediaRepository
import com.example.moviehub.utils.DispatcherProvider
import com.example.moviehub.utils.Resource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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

    sealed class GetBaseImageUrlEvent {
        class Success(val resultText: String): GetBaseImageUrlEvent()
        class Failure(val errorText: String): GetBaseImageUrlEvent()
        class Exception(val exceptionText: String): GetBaseImageUrlEvent()
        object Loading: GetBaseImageUrlEvent()
        object Empty: GetBaseImageUrlEvent()
    }

    sealed class GetImageSizeEvent {
        class Success(val resultText: String): GetImageSizeEvent()
        class Failure(val errorText: String): GetImageSizeEvent()
        class Exception(val exceptionText: String): GetImageSizeEvent()
        object Loading: GetImageSizeEvent()
        object Empty: GetImageSizeEvent()
    }

    sealed class GetTrendingShowsByPageEvent {
        class Success(val resultText: String): GetTrendingShowsByPageEvent()
        class Failure(val errorText: String): GetTrendingShowsByPageEvent()
        class Exception(val exceptionText: String): GetTrendingShowsByPageEvent()
        object Loading: GetTrendingShowsByPageEvent()
        object Empty: GetTrendingShowsByPageEvent()
    }

    sealed class GetMovieGenresEvent {
        class Success(val resultText: String): GetMovieGenresEvent()
        class Failure(val errorText: String): GetMovieGenresEvent()
        class Exception(val exceptionText: String): GetMovieGenresEvent()
        object Loading: GetMovieGenresEvent()
        object Empty: GetMovieGenresEvent()
    }

    private val _getTrendingMoviesByPageResponse = MutableStateFlow<GetTrendingMoviesByPageEvent>(GetTrendingMoviesByPageEvent.Empty)
    val getTrendingMoviesByPageResponse: StateFlow<GetTrendingMoviesByPageEvent> = _getTrendingMoviesByPageResponse

    private val _getTrendingShowsByPageResponse = MutableStateFlow<GetTrendingShowsByPageEvent>(GetTrendingShowsByPageEvent.Empty)
    val getTrendingShowsByPageResponse: StateFlow<GetTrendingShowsByPageEvent> = _getTrendingShowsByPageResponse

    private val _getBaseImageUrlResponse = MutableStateFlow<GetBaseImageUrlEvent>(GetBaseImageUrlEvent.Empty)
    val getBaseImageUrlResponse: StateFlow<GetBaseImageUrlEvent> = _getBaseImageUrlResponse

    private val _getImageSizeResponse = MutableStateFlow<GetImageSizeEvent>(GetImageSizeEvent.Empty)
    val getImageSizeResponse: StateFlow<GetImageSizeEvent> = _getImageSizeResponse

    private val _getMovieGenresResponse = MutableStateFlow<GetMovieGenresEvent>(GetMovieGenresEvent.Empty)
    val getMovieGenresResponse: StateFlow<GetMovieGenresEvent> = _getMovieGenresResponse

    var movieList = arrayListOf<MediaBody>()
    var showList = arrayListOf<MediaBody>()
    var baseImageUrl: String? = null
    var imageSize: String? = null
    var pageCounter = 1
    var movieGenres: GenreModel? = null

    fun getTrendingMoviesByPage(page: Int){
        viewModelScope.launch(dispatchers.io){
            _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getTrendingMoviesByPage(page)){
                    is Resource.Success -> {
                        movieList = response.data!!.results
                        _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Success("Trending Movies Retrieved")
                    }
                    is Resource.Error -> _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Failure(response.message!!)
                    is Resource.Exception -> _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Exception(response.message!!)
                }
            }else _getTrendingMoviesByPageResponse.value = GetTrendingMoviesByPageEvent.Failure("No internet connection")
        }
    }

    fun getTrendingShowsByPage(page: Int){
        viewModelScope.launch(dispatchers.io){
            _getTrendingShowsByPageResponse.value = GetTrendingShowsByPageEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getTrendingShowsByPage(page)){
                    is Resource.Success -> {
                        showList = response.data!!.results
                        _getTrendingShowsByPageResponse.value = GetTrendingShowsByPageEvent.Success("Trending Shows Retrieved")
                    }
                    is Resource.Error -> _getTrendingShowsByPageResponse.value = GetTrendingShowsByPageEvent.Failure(response.message!!)
                    is Resource.Exception -> _getTrendingShowsByPageResponse.value = GetTrendingShowsByPageEvent.Exception(response.message!!)
                }
            }else _getTrendingShowsByPageResponse.value = GetTrendingShowsByPageEvent.Failure("No internet connection")
        }
    }

    fun getBaseImageUrl(){
        viewModelScope.launch(dispatchers.io){
            _getBaseImageUrlResponse.value = GetBaseImageUrlEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getBaseImageUrl()){
                    is Resource.Success -> {
                        baseImageUrl = response.data?.string()
                        _getBaseImageUrlResponse.value = GetBaseImageUrlEvent.Success("Base Image Url Retrieved")
                    }
                    is Resource.Error -> _getBaseImageUrlResponse.value = GetBaseImageUrlEvent.Failure(response.message!!)
                    is Resource.Exception -> _getBaseImageUrlResponse.value = GetBaseImageUrlEvent.Exception(response.message!!)
                }
            }else _getBaseImageUrlResponse.value = GetBaseImageUrlEvent.Failure("No internet connection")
        }
    }

    fun getImageSize(){
        viewModelScope.launch(dispatchers.io){
            _getImageSizeResponse.value = GetImageSizeEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getImageSize()){
                    is Resource.Success -> {
                        imageSize = response.data?.string()
                        _getImageSizeResponse.value = GetImageSizeEvent.Success("Image Size Retrieved")
                    }
                    is Resource.Error -> _getImageSizeResponse.value = GetImageSizeEvent.Failure(response.message!!)
                    is Resource.Exception -> _getImageSizeResponse.value = GetImageSizeEvent.Exception(response.message!!)
                }
            }else _getImageSizeResponse.value = GetImageSizeEvent.Failure("No internet connection")
        }
    }

    fun getMovieGenres() {
        viewModelScope.launch(dispatchers.io){
            _getMovieGenresResponse.value = GetMovieGenresEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getMovieGenres()){
                    is Resource.Success -> {
                        movieGenres = response.data
                        _getMovieGenresResponse.value = GetMovieGenresEvent.Success("Movie Genres Retrieved")
                    }
                    is Resource.Error -> _getMovieGenresResponse.value = GetMovieGenresEvent.Failure(response.message!!)
                    is Resource.Exception -> _getMovieGenresResponse.value = GetMovieGenresEvent.Exception(response.message!!)
                }
            }else _getMovieGenresResponse.value = GetMovieGenresEvent.Failure("No internet connection")
        }
    }
}