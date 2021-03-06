package com.example.moviehub.repository

import com.example.moviehub.models.GenreModel
import com.example.moviehub.models.ResultBody
import com.example.moviehub.models.WatchProviderBody
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

    override suspend fun getImageSizes(): Resource<ArrayList<String>> {
        return try {
            val response = api.getImageSizes()
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getMovieGenres(): Resource<GenreModel> {
        return try {
            val response = api.getMovieGenres()
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

    override suspend fun getGenreMoviesByPage(page: Int, genre: String): Resource<ResultBody> {
        return try {
            val response = api.getGenreMoviesByPage(page, genre)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getGenreShowsByPage(page: Int, genre: String): Resource<ResultBody> {
        return try {
            val response = api.getGenreShowsByPage(page, genre)
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

    override suspend fun getMovieProviders(movieId: Int): Resource<WatchProviderBody> {
        return try {
            val response = api.getMovieProviders(movieId)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getShowProviders(showId: Int): Resource<WatchProviderBody> {
        return try {
            val response = api.getShowProviders(showId)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }

    override suspend fun getMoviesBasedOnFilter(
        page: Int,
        providers: String,
        genres: String
    ): Resource<ResultBody> {
        return try {
            val response = api.getMoviesBasedOnFilter(page, providers, genres)
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