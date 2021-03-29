package com.example.moviehub.models

data class ResultBody(
    val page: Int,
    val results: ArrayList<MediaBody>,
    val total_pages: Int,
    val total_results: Int
)