package com.example.moviehub.viewModels

import androidx.lifecycle.ViewModel
import com.example.moviehub.models.MediaBody
import java.util.concurrent.ConcurrentHashMap

class SharedViewModel: ViewModel() {

    var movieList = arrayListOf<MediaBody>()
    var showList = arrayListOf<MediaBody>()
    var listOfGenresMovies = ConcurrentHashMap<String, MutableList<MediaBody>>()
    var listOfGenresShows = ConcurrentHashMap<String, MutableList<MediaBody>>()
}