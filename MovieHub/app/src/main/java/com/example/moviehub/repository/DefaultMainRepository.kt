package com.example.moviehub.repository

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.ResultBody
import com.example.moviehub.models.UserBody
import com.example.moviehub.retrofit.MainApiService
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: MainApiService
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

    override suspend fun addMovieToWatchlist(username: String, movieID: Int): Resource<ResponseBody> {
        return try {
            val response = api.addMovieToWatchlist(username, movieID)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun addShowToWatchlist(username: String, showID: Int): Resource<ResponseBody> {
        return try {
            val response = api.addShowToWatchlist(username, showID)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun removeMovieFromWatchlist(username: String, movieID: Int): Resource<ResponseBody> {
        return try {
            val response = api.removeMovieFromWatchlist(username, movieID)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun removeShowFromWatchlist(username: String, showID: Int): Resource<ResponseBody> {
        return try {
            val response = api.removeShowFromWatchlist(username, showID)
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