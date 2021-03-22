package com.example.moviehub.retrofit

import com.example.moviehub.models.GenreModel
import com.example.moviehub.models.ResultBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApiService {

    /***
     * Media api requests start
     */

    @GET("media/baseImageUrl")
    suspend fun getBaseImageUrl(): Response<ResponseBody>

    @GET("media/imageSizes")
    suspend fun getImageSizes(): Response<ArrayList<String>>

    @GET("media/movie/genre")
    suspend fun getMovieGenres(): Response<GenreModel>

    @GET("media/trending/movie/{page}")
    suspend fun getTrendingMoviesByPage(@Path("page") page: Int): Response<ResultBody>

    @GET("media/trending/show/{page}")
    suspend fun getTrendingShowsByPage(@Path("page") page: Int): Response<ResultBody>

    @GET("media/discover/movie")
    suspend fun getMoviesBasedOnFilter(@Query("page") page: Int, @Query("with_watch_providers")providers: String, @Query("with_genres")genres: String): Response<ResultBody>

    /***
     * Media api requests end
     */
}