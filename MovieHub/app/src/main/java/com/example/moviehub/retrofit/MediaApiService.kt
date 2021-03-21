package com.example.moviehub.retrofit

import com.example.moviehub.models.ResultBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MediaApiService {

    /***
     * Media api requests start
     */

    @GET("media/trending/movies/{page}")
    suspend fun getTrendingMoviesByPage(@Path("page") page: Int): Response<ResultBody>

    /***
     * Media api requests end
     */
}