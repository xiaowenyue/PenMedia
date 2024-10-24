package com.example.penmediatv.Data

data class TrendRecommendResponse(
    val code: String,
    val message: String,
    val data: List<TrendRecommendItem>
)

data class TrendRecommendItem(
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val episode: Int,
    val duration: Int,
    val videoId: String,
    val subTitle: String?,
    val videoType: String,
    val videoDesc: String?,
    val otherInfo: TrendRecommendOtherInfo
)

data class TrendRecommendOtherInfo(
    val region: String,
    val category: String,
    val director: String,
    val mainActors: String,
    val releaseDate: String
)

data class SearchRequest(
    val page: Int,
    val pageSize: Int,
    val searchList: List<String>
)

data class SearchResponse(
    val code: String,
    val message: String,
    val data: SearchData
)

data class SearchData(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Int,
    val records: List<TrendRecommendItem>
)