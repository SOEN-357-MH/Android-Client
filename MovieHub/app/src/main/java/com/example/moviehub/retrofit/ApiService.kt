package com.example.moviehub.retrofit

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.ResultBody
import com.example.moviehub.models.UserBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /***
     * User api requests start
     */

    @POST("account/auth")
    suspend fun loginUser(@Body loginBody: LoginBody): Response<ResponseBody>

    @PUT("account/user")
    suspend fun registerUser(@Body userBody: UserBody): Response<ResponseBody>

    /***
     * User api requests end
     */

    /***
     * Media api requests start
     */

    @GET("media/trending/movies/{page}")
    suspend fun getTrendingMoviesByPage(@Path("page") page: Int): Response<ResultBody>

    /***
     * Media api requests end
     */
}