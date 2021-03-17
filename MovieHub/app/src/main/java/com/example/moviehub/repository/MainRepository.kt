package com.example.moviehub.repository

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.UserBody
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody

interface MainRepository {

    suspend fun loginUser(loginBody: LoginBody): Resource<ResponseBody>

    suspend fun registerUser(userBody: UserBody): Resource<ResponseBody>
}