package com.example.penmediatv

data class Movie(
    val name: String,
    val imageResId: Int,
    val details: String = "",
    val minorDetails: String = ""
)
