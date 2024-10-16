package com.example.penmediatv.Data

data class CollectionAddRequest(
    val deviceId: String,
    val videoId: String
)

data class CollectionAddResponse(
    val code: String,
    val message: String
)
