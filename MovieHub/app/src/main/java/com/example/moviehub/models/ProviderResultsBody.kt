package com.example.moviehub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProviderResultsBody(
    val CA: ArModel
): Parcelable
