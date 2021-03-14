package com.example.moviehub.repository

import com.example.moviehub.retrofit.ApiService
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: ApiService
): MainRepository {
}