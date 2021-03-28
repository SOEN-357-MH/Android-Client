package com.example.moviehub.viewModels

import androidx.lifecycle.ViewModel
import com.example.moviehub.models.MediaBody

class SharedViewModel: ViewModel() {

    var movieList = arrayListOf<MediaBody>()
}