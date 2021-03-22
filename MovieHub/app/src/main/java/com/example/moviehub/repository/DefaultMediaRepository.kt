package com.example.moviehub.repository

import com.example.moviehub.models.ResultBody
import com.example.moviehub.retrofit.MediaApiService
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody
import java.lang.Exception
import javax.inject.Inject

class DefaultMediaRepository @Inject constructor(
    private val api: MediaApiService
): MediaRepository {

    override suspend fun getBaseImageUrl(): Resource<ResponseBody> {
        return try {
            val response = api.getBaseImageUrl()
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getImageSize(): Resource<ResponseBody> {
        return try {
            val response = api.getImageSize()
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getTrendingMoviesByPage(page: Int): Resource<ResultBody> {
        return try {
            val response = api.getTrendingMoviesByPage(page)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getTrendingShowsByPage(page: Int): Resource<ResultBody> {
        return try {
            val response = api.getTrendingShowsByPage(page)
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