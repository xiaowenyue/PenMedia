package com.example.penmediatv.Data

data class CollectionAddRequest(
    val deviceId: String,
    val videoId: String
)

data class CollectionAddResponse(
    val code: String,
    val message: String
)

data class CollectionResponse(
    val code: String,
    val message: String,
    val data: CollectionData
)

data class CollectionData(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Int,
    val records: List<CollectionItem>
)

data class CollectionItem(
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val videoId: String
)

data class CollectionClearRequest(
    val deviceId: String
)