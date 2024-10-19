package com.example.penmediatv.Data

data class EpisodeResponse(
    val code: String,
    val message: String,
    val data: EpisodeData
)

data class EpisodeData(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Int,
    val records: List<EpisodeItem>
)

data class EpisodeItem(
    val videoId: String,
    val videoUrl: String,
    val episode: Int,
    val duration: Int
)