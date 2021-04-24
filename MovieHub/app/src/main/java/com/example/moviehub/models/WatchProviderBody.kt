package com.example.moviehub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WatchProviderBody(
    val id: Int,
    val results: ProviderResultsBody?
): Parcelable
