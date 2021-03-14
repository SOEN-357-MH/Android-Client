package com.example.moviehub.utils

sealed class Resource<T>(val data: T?, val message: String?){
    class Success<T>(data: T, message: String?): Resource<T>(data, message)
    class Error<T>(message: String): Resource<T>(null, message)
    class Exception<T>(message: String): Resource<T>(null, message)
}
