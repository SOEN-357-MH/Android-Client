package com.example.moviehub.repository

import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.ResultBody
import com.example.moviehub.models.UserBody
import com.example.moviehub.utils.Resource
import okhttp3.ResponseBody

interface MainRepository {

    /***
     * User api requests start
     */

    suspend fun loginUser(loginBody: LoginBody): Resource<ResponseBody>

    suspend fun registerUser(userBody: UserBody): Resource<ResponseBody>

    suspend fun addMovieToWatchlist(username: String, movieID: Int): Resource<ResponseBody>

    suspend fun addShowToWatchlist(username: String, showID: Int): Resource<ResponseBody>

    suspend fun removeMovieFromWatchlist(username: String, movieID: Int): Resource<ResponseBody>

    suspend fun removeShowFromWatchlist(username: String, showID: Int): Resource<ResponseBody>

    /***
     * User api requests end
     */
}