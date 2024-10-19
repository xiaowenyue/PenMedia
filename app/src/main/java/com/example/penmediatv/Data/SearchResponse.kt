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
    val otherInfo: TrendRecommendOtherInfo
)

data class TrendRecommendOtherInfo(
    val region: String,
    val category: String,
    val director: String,
    val mainActors: String,
    val releaseDate: String
)

