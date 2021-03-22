package com.example.moviehub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MediaBody(
    val id: Int,
    val release_date: String,
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: ArrayList<Int>,
    val original_language: String,
    var poster_path: String,
    var title: String,
    val overview: String,
    val media_type: String,
    val origin_country: ArrayList<String>,
    val name: String,
    val first_air_date: String,
    val genres: ArrayList<String>,
    var providers: WatchProviderBody?
) : Parcelable
