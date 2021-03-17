package com.example.moviehub.retrofit

import com.example.moviehub.models.UserBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface ApiService {

    @PUT("account/addUser")
    suspend fun registerUser(@Body userBody: UserBody): Response<ResponseBody>
}