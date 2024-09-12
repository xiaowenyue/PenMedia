package com.example.penmediatv

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val name: String,
    val imageResId: Int,
    val details: String = "",
    val minorDetails: String = "",
    val episodes: Int = 0
): Parcelable
