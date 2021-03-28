package com.example.moviehub.repository

import com.example.moviehub.models.GenreModel
import com.example.moviehub.models.ResultBody
import com.example.moviehub.models.WatchProviderBody
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody

interface MediaRepository {

    /***
     * Media api requests start
     */

    suspend fun getBaseImageUrl(): Resource<ResponseBody>

    suspend fun getImageSizes(): Resource<ArrayList<String>>

    suspend fun getMovieGenres(): Resource<GenreModel>

    suspend fun getTrendingMoviesByPage(page: Int): Resource<ResultBody>

    suspend fun getTrendingShowsByPage(page: Int): Resource<ResultBody>

    suspend fun getMovieProviders(movieId: Int): Resource<WatchProviderBody>

    suspend fun getShowProviders(showId: Int): Resource<WatchProviderBody>

    suspend fun getMoviesBasedOnFilter(page: Int, providers: String, genres: String): Resource<ResultBody>


    /***
     * Media api requests end
     */
}