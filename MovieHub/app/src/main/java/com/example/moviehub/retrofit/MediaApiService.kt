package com.example.moviehub.retrofit

import com.example.moviehub.models.ResultBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MediaApiService {

    /***
     * Media api requests start
     */

    @GET("media/baseImageUrl")
    suspend fun getBaseImageUrl(): Response<ResponseBody>

    @GET("media/imageSize")
    suspend fun getImageSize(): Response<ResponseBody>

    @GET("media/trending/movie/{page}")
    suspend fun getTrendingMoviesByPage(@Path("page") page: Int): Response<ResultBody>

    /***
     * Media api requests end
     */
}