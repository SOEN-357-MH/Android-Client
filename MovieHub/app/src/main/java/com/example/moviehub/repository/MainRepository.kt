package com.example.moviehub.repository

import com.example.moviehub.models.UserBody
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody

interface MainRepository {

    suspend fun registerUser(userBody: UserBody): Resource<ResponseBody>
}