package com.example.penmediatv.Data

data class InfoResponse(
    val code: String,
    val message: String,
    val data: List<InfoData>
)

data class InfoData(
    val content: String,
    val contentTitle: String
)
