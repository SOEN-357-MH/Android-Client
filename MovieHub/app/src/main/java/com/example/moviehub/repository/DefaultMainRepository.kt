package com.example.moviehub.repository

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.UserBody
import com.example.moviehub.retrofit.ApiService
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: ApiService
): MainRepository {

    override suspend fun loginUser(loginBody: LoginBody): Resource<ResponseBody> {
        return try {
            val response = api.loginUser(loginBody)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun registerUser(userBody: UserBody): Resource<ResponseBody> {
        return try {
            val response = api.registerUser(userBody)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }
}