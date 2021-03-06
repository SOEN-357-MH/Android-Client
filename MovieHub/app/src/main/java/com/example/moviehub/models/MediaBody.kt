package com.example.moviehub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MediaBody(
    val id: Int,
    var release_date: String,
    val adult: Boolean,
    var backdrop_path: String,
    val genre_ids: ArrayList<Int>,
    val original_language: String,
    var poster_path: String,
    var title: String,
    val overview: String,
    var media_type: String,
    val origin_country: ArrayList<String>,
    val name: String,
    val first_air_date: String,
    val genres: ArrayList<String>,
    var providers: WatchProviderBody?
) : Parcelable{
    var isWatched: Boolean = true
}
