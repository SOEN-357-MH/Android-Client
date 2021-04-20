package com.example.moviehub.retrofit

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.UserBody
import com.example.moviehub.models.WatchProviderBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MainApiService {

    /***
     * User api requests start
     */

    @POST("account/auth")
    suspend fun loginUser(@Body loginBody: LoginBody): Response<ResponseBody>

    @PUT("account/user")
    suspend fun registerUser(@Body userBody: UserBody): Response<ResponseBody>

    @PUT("account/user/{username}/watchlist/movie/{movieId}")
    suspend fun addMovieToWatchlist(@Path("username") username: String, @Path("movieId")movieId: Int): Response<ResponseBody>

    @PUT("account/user/{username}/watchlist/show/{showId}")
    suspend fun addShowToWatchlist(@Path("username") username: String, @Path("showId")showId: Int): Response<ResponseBody>
    
    /***
     * User api requests end
     */
}