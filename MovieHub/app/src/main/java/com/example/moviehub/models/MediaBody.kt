package com.example.moviehub.models

data class MediaBody(
    val id: Int,
    val release_date: String,
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: ArrayList<Int>,
    val original_language: String,
    val poster_path: String,
    val title: String,
    val Overview: String,
    val media_type: String,
    val origin_country: ArrayList<String>,
    val name: String,
    val first_air_date: String,
    val genres: ArrayList<String>

)
