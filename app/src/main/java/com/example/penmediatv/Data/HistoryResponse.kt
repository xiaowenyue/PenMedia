package com.example.penmediatv.Data

data class HistoryResponse(
    val code: String,
    val message: String,
    val data: HistoryData
)

data class HistoryData(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Int,
    val records: List<HistoryItem>
)

data class HistoryItem(
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val videoUrl: String,
    val videoId: String,
    val playedDuration: Int,
    val duration: Int
)

data class HistoryAddRequest(
    val deviceId: String,
    val videoId: String,
    val playDuration: Int
)

data class HistoryAddResponse(
    val code: String,
    val message: String
)

data class HistoryClearRequest(
    val deviceId: String
)