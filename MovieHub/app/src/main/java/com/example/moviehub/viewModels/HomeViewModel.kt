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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val dispatchers: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val userRepository: MainRepository
): ViewModel() {

    sealed class AddMovieToWatchlistEvent {
        class Success(val resultText: String): AddMovieToWatchlistEvent()
        class Failure(val errorText: String): AddMovieToWatchlistEvent()
        class Exception(val exceptionText: String): AddMovieToWatchlistEvent()
        object Loading: AddMovieToWatchlistEvent()
        object Empty: AddMovieToWatchlistEvent()
    }

    sealed class AddShowToWatchlistEvent {
        class Success(val resultText: String): AddShowToWatchlistEvent()
        class Failure(val errorText: String): AddShowToWatchlistEvent()
        class Exception(val exceptionText: String): AddShowToWatchlistEvent()
        object Loading: AddShowToWatchlistEvent()
        object Empty: AddShowToWatchlistEvent()
    }

    sealed class RemoveMovieFromWatchlistEvent {
        class Success(val resultText: String): RemoveMovieFromWatchlistEvent()
        class Failure(val errorText: String): RemoveMovieFromWatchlistEvent()
        class Exception(val exceptionText: String): RemoveMovieFromWatchlistEvent()
        object Loading: RemoveMovieFromWatchlistEvent()
        object Empty: RemoveMovieFromWatchlistEvent()
    }

    sealed class RemoveShowFromWatchlistEvent {
        class Success(val resultText: String): RemoveShowFromWatchlistEvent()
        class Failure(val errorText: String): RemoveShowFromWatchlistEvent()
        class Exception(val exceptionText: String): RemoveShowFromWatchlistEvent()
        object Loading: RemoveShowFromWatchlistEvent()
        object Empty: RemoveShowFromWatchlistEvent()
    }

    sealed class GetTrendingMoviesByPageEvent {
        class Success(val resultText: String): GetTrendingMoviesByPageEvent()
        class Failure(val errorText: String): GetTrendingMoviesByPageEvent()
        class Exception(val exceptionText: String): GetTrendingMoviesByPageEvent()
        object Loading: GetTrendingMoviesByPageEvent()
        object Empty: GetTrendingMoviesByPageEvent()
    }

    sealed class GetGenreMoviesByPageEvent {
        class Success(val resultText: String): GetGenreMoviesByPageEvent()
        class Failure(val errorText: String): GetGenreMoviesByPageEvent()
        class Exception(val exceptionText: String): GetGenreMoviesByPageEvent()
        object Loading: GetGenreMoviesByPageEvent()
        object Empty: GetGenreMoviesByPageEvent()
    }

    sealed class GetGenreShowsByPageEvent {
        class Success(val resultText: String): GetGenreShowsByPageEvent()
        class Failure(val errorText: String): GetGenreShowsByPageEvent()
        class Exception(val exceptionText: String): GetGenreShowsByPageEvent()
        object Loading: GetGenreShowsByPageEvent()
        object Empty: GetGenreShowsByPageEvent()
    }

    sealed class GetBaseImageUrlEvent {
        class Success(val resultText: String): GetBaseImageUrlEvent()
        class Failure(val errorText: String): GetBaseImageUrlEvent()
        class Exception(val exceptionText: String): GetBaseImageUrlEvent()
        object Loading: GetBaseImageUrlEvent()
        object Empty: GetBaseImageUrlEvent()
    }

    sealed class GetImageSizesEvent {
        class Success(val resultText: String): GetImageSizesEvent()
        class Failure(val errorText: String): GetImageSizesEvent()
        class Exception(val exceptionText: String): GetImageSizesEvent()
        object Loading: GetImageSizesEvent()
        object Empty: GetImageSizesEvent()
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

    sealed class GetMovieProvidersEvent {
        class Success(val resultText: String): GetMovieProvidersEvent()
        class Failure(val errorText: String): GetMovieProvidersEvent()
        class Exception(val exceptionText: String): GetMovieProvidersEvent()
        object Loading: GetMovieProvidersEvent()
        object Empty: GetMovieProvidersEvent()
    }

    sealed class GetShowProvidersEvent {
        class Success(val resultText: String): GetShowProvidersEvent()
        class Failure(val errorText: String): GetShowProvidersEvent()
        class Exception(val exceptionText: String): GetShowProvidersEvent()
        object Loading: GetShowProvidersEvent()
        object Empty: GetShowProvidersEvent()
    }

    sealed class DiscoverMovieEvent {
        class Success(val resultText: String): DiscoverMovieEvent()
        class Failure(val errorText: String): DiscoverMovieEvent()
        class Exception(val exceptionText: String): DiscoverMovieEvent()
        object Loading: DiscoverMovieEvent()
        object Empty: DiscoverMovieEvent()
    }

    sealed class GetMovieWatchlistEvent {
        class Success(val resultText: String): GetMovieWatchlistEvent()
        class Failure(val errorText: String): GetMovieWatchlistEvent()
        class Exception(val exceptionText: String): GetMovieWatchlistEvent()
        object Loading: GetMovieWatchlistEvent()
        object Empty: GetMovieWatchlistEvent()
    }

    sealed class GetShowWatchlistEvent {
        class Success(val resultText: String): GetShowWatchlistEvent()
        class Failure(val errorText: String): GetShowWatchlistEvent()
        class Exception(val exceptionText: String): GetShowWatchlistEvent()
        object Loading: GetShowWatchlistEvent()
        object Empty: GetShowWatchlistEvent()
    }

    private val _getTrendingMoviesByPageResponse = MutableStateFlow<GetTrendingMoviesByPageEvent>(GetTrendingMoviesByPageEvent.Empty)
    val getTrendingMoviesByPageResponse: StateFlow<GetTrendingMoviesByPageEvent> = _getTrendingMoviesByPageResponse

    private val _getGenreMoviesByPageResponse = MutableStateFlow<GetGenreMoviesByPageEvent>(GetGenreMoviesByPageEvent.Empty)
    val getGenreMoviesByPageResponse: StateFlow<GetGenreMoviesByPageEvent> = _getGenreMoviesByPageResponse

    private val _getGenreShowsByPageResponse = MutableStateFlow<GetGenreShowsByPageEvent>(GetGenreShowsByPageEvent.Empty)
    val getGenreShowsByPageResponse: StateFlow<GetGenreShowsByPageEvent> = _getGenreShowsByPageResponse

    private val _getTrendingShowsByPageResponse = MutableStateFlow<GetTrendingShowsByPageEvent>(GetTrendingShowsByPageEvent.Empty)
    val getTrendingShowsByPageResponse: StateFlow<GetTrendingShowsByPageEvent> = _getTrendingShowsByPageResponse

    private val _getBaseImageUrlResponse = MutableStateFlow<GetBaseImageUrlEvent>(GetBaseImageUrlEvent.Empty)
    val getBaseImageUrlResponse: StateFlow<GetBaseImageUrlEvent> = _getBaseImageUrlResponse

    private val _getImageSizeResponse = MutableStateFlow<GetImageSizesEvent>(GetImageSizesEvent.Empty)
    val getImageSizeResponse: StateFlow<GetImageSizesEvent> = _getImageSizeResponse

    private val _getMovieGenresResponse = MutableStateFlow<GetMovieGenresEvent>(GetMovieGenresEvent.Empty)
    val getMovieGenresResponse: StateFlow<GetMovieGenresEvent> = _getMovieGenresResponse

    private val _getMovieProvidersResponse = MutableStateFlow<GetMovieProvidersEvent>(GetMovieProvidersEvent.Empty)
    val getMovieProvidersResponse: StateFlow<GetMovieProvidersEvent> = _getMovieProvidersResponse

    private val _getShowProvidersResponse = MutableStateFlow<GetShowProvidersEvent>(GetShowProvidersEvent.Empty)
    val getShowProvidersResponse: StateFlow<GetShowProvidersEvent> = _getShowProvidersResponse

    private val _discoverMovieResponse = MutableStateFlow<DiscoverMovieEvent>(DiscoverMovieEvent.Empty)
    val discoverMovieResponse: StateFlow<DiscoverMovieEvent> = _discoverMovieResponse

    private val _addMovieToWatchlistResponse = MutableStateFlow<AddMovieToWatchlistEvent>(AddMovieToWatchlistEvent.Empty)
    val addMovieToWatchlistResponse: StateFlow<AddMovieToWatchlistEvent> = _addMovieToWatchlistResponse

    private val _addShowToWatchlistResponse = MutableStateFlow<AddShowToWatchlistEvent>(AddShowToWatchlistEvent.Empty)
    val addShowToWatchlistResponse: StateFlow<AddShowToWatchlistEvent> = _addShowToWatchlistResponse

    private val _removeMovieFromWatchlistResponse = MutableStateFlow<RemoveMovieFromWatchlistEvent>(RemoveMovieFromWatchlistEvent.Empty)
    val removeMovieFromWatchlistResponse: StateFlow<RemoveMovieFromWatchlistEvent> = _removeMovieFromWatchlistResponse

    private val _removeShowFromWatchlistResponse = MutableStateFlow<RemoveShowFromWatchlistEvent>(RemoveShowFromWatchlistEvent.Empty)
    val removeShowFromWatchlistResponse: StateFlow<RemoveShowFromWatchlistEvent> = _removeShowFromWatchlistResponse

    private val _getMovieWatchlistResponse = MutableStateFlow<GetMovieWatchlistEvent>(GetMovieWatchlistEvent.Empty)
    val getMovieWatchlistResponse: StateFlow<GetMovieWatchlistEvent> = _getMovieWatchlistResponse

    private val _getShowWatchlistResponse = MutableStateFlow<GetShowWatchlistEvent>(GetShowWatchlistEvent.Empty)
    val getShowWatchlistResponse: StateFlow<GetShowWatchlistEvent> = _getShowWatchlistResponse

    var movieList = arrayListOf<MediaBody>()
    var showList = arrayListOf<MediaBody>()

    var movieWatchList = arrayListOf<MediaBody>()
    var showWatchList = arrayListOf<MediaBody>()

    var baseImageUrl: String? = null
    var imageSizes = arrayListOf<String>()
    var movieGenres: GenreModel? = null
    var selectedTab = 0
    var genresLoadedMovies = 0
    var genresLoadedShows = 0

    var listOfGenresMovies = hashMapOf<String, MutableList<MediaBody>>()
    var listOfGenresShows = hashMapOf<String, MutableList<MediaBody>>()

    var movieGenreMap = hashMapOf<String, String>()
    var showGenreMap = hashMapOf<String, String>()

    fun addMovieToWatchlist(username: String, movieID: Int){
        viewModelScope.launch(dispatchers.io){
            _addMovieToWatchlistResponse.value = AddMovieToWatchlistEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = userRepository.addMovieToWatchlist(username, movieID)){
                    is Resource.Success -> {
                        _addMovieToWatchlistResponse.value = AddMovieToWatchlistEvent.Success("Movie Added to Watchlist")
                    }
                    is Resource.Error -> _addMovieToWatchlistResponse.value = AddMovieToWatchlistEvent.Failure(response.message!!)
                    is Resource.Exception -> _addMovieToWatchlistResponse.value = AddMovieToWatchlistEvent.Exception(response.message!!)
                }
            }else _addMovieToWatchlistResponse.value = AddMovieToWatchlistEvent.Failure("No internet connection")
        }
    }

    fun addShowToWatchlist(username: String, showID: Int){
        viewModelScope.launch(dispatchers.io){
            _addShowToWatchlistResponse.value = AddShowToWatchlistEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = userRepository.addShowToWatchlist(username, showID)){
                    is Resource.Success -> {
                        _addShowToWatchlistResponse.value = AddShowToWatchlistEvent.Success("Show Added to Watchlist")
                    }
                    is Resource.Error -> _addShowToWatchlistResponse.value = AddShowToWatchlistEvent.Failure(response.message!!)
                    is Resource.Exception -> _addShowToWatchlistResponse.value = AddShowToWatchlistEvent.Exception(response.message!!)
                }
            }else _addShowToWatchlistResponse.value = AddShowToWatchlistEvent.Failure("No internet connection")
        }
    }

    fun removeMovieFromWatchlist(username: String, movieID: Int){
        viewModelScope.launch(dispatchers.io){
            _removeMovieFromWatchlistResponse.value = RemoveMovieFromWatchlistEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = userRepository.removeMovieFromWatchlist(username, movieID)){
                    is Resource.Success -> {
                        _removeMovieFromWatchlistResponse.value = RemoveMovieFromWatchlistEvent.Success("Movie Removed from Watchlist")
                    }
                    is Resource.Error -> _removeMovieFromWatchlistResponse.value = RemoveMovieFromWatchlistEvent.Failure(response.message!!)
                    is Resource.Exception -> _removeMovieFromWatchlistResponse.value = RemoveMovieFromWatchlistEvent.Exception(response.message!!)
                }
            }else _removeMovieFromWatchlistResponse.value = RemoveMovieFromWatchlistEvent.Failure("No internet connection")
        }
    }

    fun removeShowFromWatchlist(username: String, showID: Int){
        viewModelScope.launch(dispatchers.io){
            _removeShowFromWatchlistResponse.value = RemoveShowFromWatchlistEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = userRepository.removeShowFromWatchlist(username, showID)){
                    is Resource.Success -> {
                        _removeShowFromWatchlistResponse.value = RemoveShowFromWatchlistEvent.Success("Show Removed from Watchlist")
                    }
                    is Resource.Error -> _removeShowFromWatchlistResponse.value = RemoveShowFromWatchlistEvent.Failure(response.message!!)
                    is Resource.Exception -> _removeShowFromWatchlistResponse.value = RemoveShowFromWatchlistEvent.Exception(response.message!!)
                }
            }else _removeShowFromWatchlistResponse.value = RemoveShowFromWatchlistEvent.Failure("No internet connection")
        }
    }

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

    fun getGenreMoviesByPage(page: Int, genre: String){

        movieGenreMap["28"] = "Action Movies"
        movieGenreMap["12"] = "Adventure Movies"
        movieGenreMap["16"] = "Animation Movies"
        movieGenreMap["35"] = "Comedy Movies"
        movieGenreMap["80"] = "Crime Movies"

        viewModelScope.launch(dispatchers.io){
            _getGenreMoviesByPageResponse.value = GetGenreMoviesByPageEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getGenreMoviesByPage(page, genre)){
                    is Resource.Success -> {

                        genresLoadedMovies++

                        if (listOfGenresMovies.containsKey(genre)){

                            response.data?.let {
                                var currentList = listOfGenresMovies[genre]
                                var newValues = it.results
                                var newList = ArrayList<MediaBody>()
                                newList.addAll(currentList!!)
                                newList.addAll(newValues)
                                listOfGenresMovies.remove(genre)
                                listOfGenresMovies.put(genre, newList)
                            }

                        } else response.data?.let { listOfGenresMovies.put(genre, it.results) }


                        _getGenreMoviesByPageResponse.value = GetGenreMoviesByPageEvent.Success("Genre Movies Retrieved")
                    }
                    is Resource.Error -> _getGenreMoviesByPageResponse.value = GetGenreMoviesByPageEvent.Failure(response.message!!)
                    is Resource.Exception -> _getGenreMoviesByPageResponse.value = GetGenreMoviesByPageEvent.Exception(response.message!!)
                }
            }else _getGenreMoviesByPageResponse.value = GetGenreMoviesByPageEvent.Failure("No internet connection")
        }
    }

    fun getGenreShowsByPage(page: Int, genre: String){

        showGenreMap["10759"] = "Action Shows"
        showGenreMap["16"] = "Animation Shows"
        showGenreMap["35"] = "Comedy Shows"
        showGenreMap["80"] = "Crime Shows"
        showGenreMap["99"] = "Documentary Shows"

        viewModelScope.launch(dispatchers.io){
            _getGenreShowsByPageResponse.value = GetGenreShowsByPageEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getGenreShowsByPage(page, genre)){
                    is Resource.Success -> {

                        genresLoadedShows++

                        if (listOfGenresShows.containsKey(genre)){

                            response.data?.let {
                                var currentList = listOfGenresShows[genre]
                                var newValues = it.results
                                var newList = ArrayList<MediaBody>()
                                newList.addAll(currentList!!)
                                newList.addAll(newValues)
                                listOfGenresShows.remove(genre)
                                listOfGenresShows.put(genre, newList)
                            }

                        } else response.data?.let { listOfGenresShows.put(genre, it.results) }
                        _getGenreShowsByPageResponse.value = GetGenreShowsByPageEvent.Success("Genre Shows Retrieved")
                    }
                    is Resource.Error -> _getGenreShowsByPageResponse.value = GetGenreShowsByPageEvent.Failure(response.message!!)
                    is Resource.Exception -> _getGenreShowsByPageResponse.value = GetGenreShowsByPageEvent.Exception(response.message!!)
                }
            }else _getGenreShowsByPageResponse.value = GetGenreShowsByPageEvent.Failure("No internet connection")
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

    fun getImageSizes(){
        viewModelScope.launch(dispatchers.io){
            _getImageSizeResponse.value = GetImageSizesEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getImageSizes()){
                    is Resource.Success -> {
                        imageSizes = response.data!!
                        _getImageSizeResponse.value = GetImageSizesEvent.Success("Image Size Retrieved")
                    }
                    is Resource.Error -> _getImageSizeResponse.value = GetImageSizesEvent.Failure(response.message!!)
                    is Resource.Exception -> _getImageSizeResponse.value = GetImageSizesEvent.Exception(response.message!!)
                }
            }else _getImageSizeResponse.value = GetImageSizesEvent.Failure("No internet connection")
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

    fun getMovieProviders(movieId: Int) {
        viewModelScope.launch(dispatchers.io){
            _getMovieProvidersResponse.value = GetMovieProvidersEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getMovieProviders(movieId)){
                    is Resource.Success -> {
                        movieList.find { it.id == movieId }?.providers = response.data
                        movieList.find { it.id == movieId }?.providers?.results?.let {
                            it.CA?.flatrate?.let { providers ->
                                for (provider in providers) {
                                    provider.logo_path = "${baseImageUrl}${imageSizes[6]}${provider.logo_path}"
                                }
                            }
                        }

                        for ((key, value) in listOfGenresMovies) {
                            //FOR EVERY GENRE IN LIST OF GENRES
                            value.find { it.id == movieId }?.providers = response.data
                            value.find { it.id == movieId }?.providers?.results?.let {
                                it.CA?.flatrate?.let { providers ->
                                    for (provider in providers) {
                                        provider.logo_path = "${baseImageUrl}${imageSizes[6]}${provider.logo_path}"
                                    }
                                }
                            }
                        }

                        movieWatchList.find { it.id == movieId }?.providers = response.data
                        movieWatchList.find { it.id == movieId }?.providers?.results?.let {
                            it.CA?.flatrate?.let { providers ->
                                for (provider in providers) {
                                    provider.logo_path = "${baseImageUrl}${imageSizes[6]}${provider.logo_path}"
                                }
                            }
                        }

                        _getMovieProvidersResponse.value = GetMovieProvidersEvent.Success("Movie Providers Retrieved")
                    }
                    is Resource.Error -> _getMovieProvidersResponse.value = GetMovieProvidersEvent.Failure(response.message!!)
                    is Resource.Exception -> _getMovieProvidersResponse.value = GetMovieProvidersEvent.Exception(response.message!!)
                }
            }else _getMovieProvidersResponse.value = GetMovieProvidersEvent.Failure("No internet connection")
        }
    }

    fun getShowProviders(showId: Int){
        viewModelScope.launch(dispatchers.io){
            _getShowProvidersResponse.value = GetShowProvidersEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getShowProviders(showId)){
                    is Resource.Success -> {
                        showList.find { it.id == showId }?.providers = response.data
                        showList.find { it.id == showId }?.providers?.results?.let {
                            it.CA?.flatrate?.let { providers ->
                                for (provider in providers) {
                                    provider.logo_path = "${baseImageUrl}${imageSizes[6]}${provider.logo_path}"
                                }
                            }
                        }

                        for ((key, value) in listOfGenresShows) {
                            //FOR EVERY GENRE IN LIST OF GENRES
                            value.find { it.id == showId }?.providers = response.data
                            value.find { it.id == showId }?.providers?.results?.let {
                                it.CA?.flatrate?.let { providers ->
                                    for (provider in providers) {
                                        provider.logo_path = "${baseImageUrl}${imageSizes[6]}${provider.logo_path}"
                                    }
                                }
                            }
                        }

                        _getShowProvidersResponse.value = GetShowProvidersEvent.Success("Show Providers Retrieved")
                    }
                    is Resource.Error -> _getShowProvidersResponse.value = GetShowProvidersEvent.Failure(response.message!!)
                    is Resource.Exception -> _getShowProvidersResponse.value = GetShowProvidersEvent.Exception(response.message!!)
                }
            }else _getShowProvidersResponse.value = GetShowProvidersEvent.Failure("No internet connection")
        }
    }

    fun discoverMovie(page: Int, providers: String, genres: String){
        viewModelScope.launch(dispatchers.io){
            _discoverMovieResponse.value = DiscoverMovieEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = repository.getMoviesBasedOnFilter(page, providers, genres)){
                    is Resource.Success -> {
                        _discoverMovieResponse.value = DiscoverMovieEvent.Success("Discovered Movies")
                    }
                    is Resource.Error -> _discoverMovieResponse.value = DiscoverMovieEvent.Failure(response.message!!)
                    is Resource.Exception -> _discoverMovieResponse.value = DiscoverMovieEvent.Exception(response.message!!)
                }
            }else _discoverMovieResponse.value = DiscoverMovieEvent.Failure("No internet connection")
        }
    }

    fun getMovieWatchlist(username: String){
        viewModelScope.launch(dispatchers.io){
            _getMovieWatchlistResponse.value = GetMovieWatchlistEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = userRepository.getMovieWatchlist(username)){
                    is Resource.Success -> {
                        movieWatchList = response.data!!.results
                        _getMovieWatchlistResponse.value = GetMovieWatchlistEvent.Success("Got Movie Watchlist")
                    }
                    is Resource.Error -> _getMovieWatchlistResponse.value = GetMovieWatchlistEvent.Failure(response.message!!)
                    is Resource.Exception -> _getMovieWatchlistResponse.value = GetMovieWatchlistEvent.Exception(response.message!!)
                }
            }else _getMovieWatchlistResponse.value = GetMovieWatchlistEvent.Failure("No internet connection")
        }
    }

    fun getShowWatchlist(username: String){
        viewModelScope.launch(dispatchers.io){
            _getShowWatchlistResponse.value = GetShowWatchlistEvent.Loading
            if(networkHelper.isNetworkConnected()){
                when(val response = userRepository.getShowWatchlist(username)){
                    is Resource.Success -> {
                        showWatchList = response.data!!.results
                        _getShowWatchlistResponse.value = GetShowWatchlistEvent.Success("Got Show Watchlist")
                    }
                    is Resource.Error -> _getShowWatchlistResponse.value = GetShowWatchlistEvent.Failure(response.message!!)
                    is Resource.Exception -> _getShowWatchlistResponse.value = GetShowWatchlistEvent.Exception(response.message!!)
                }
            }else _getShowWatchlistResponse.value = GetShowWatchlistEvent.Failure("No internet connection")
        }
    }

}