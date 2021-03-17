package com.example.moviehub.retrofit

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.UserBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("account/auth")
    suspend fun loginUser(@Body loginBody: LoginBody): Response<ResponseBody>

    @PUT("account/user")
    suspend fun registerUser(@Body userBody: UserBody): Response<ResponseBody>
}