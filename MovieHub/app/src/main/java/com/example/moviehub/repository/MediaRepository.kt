package com.example.moviehub.repository

import com.example.moviehub.models.ResultBody
import com.example.moviehub.utils.Resource

interface MediaRepository {

    /***
     * Media api requests start
     */

    suspend fun getTrendingMoviesByPage(page: Int): Resource<ResultBody>

    /***
     * Media api requests end
     */
}