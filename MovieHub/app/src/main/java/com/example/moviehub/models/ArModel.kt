package com.example.moviehub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArModel(
    val link: String,
    val rent: ArrayList<AdModel>,
    val buy: ArrayList<AdModel>,
    val flatrate: ArrayList<AdModel>?,
    val ads: ArrayList<AdModel>
): Parcelable