package com.example.moviehub.models

data class UserBody(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    var password: String
)
