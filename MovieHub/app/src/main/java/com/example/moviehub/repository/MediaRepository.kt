package com.example.moviehub.repository

import com.example.moviehub.models.ResultBody
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody

interface MediaRepository {

    /***
     * Media api requests start
     */

    suspend fun getBaseImageUrl(): Resource<ResponseBody>

    suspend fun getImageSize(): Resource<ResponseBody>

    suspend fun getTrendingMoviesByPage(page: Int): Resource<ResultBody>

    /***
     * Media api requests end
     */
}