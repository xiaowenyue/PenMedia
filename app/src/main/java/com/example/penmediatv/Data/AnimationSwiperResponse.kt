package com.example.penmediatv.Data

data class SwiperResponse(
    val code: String,
    val message: String,
    val data: List<SwiperItem>
)

data class SwiperItem(
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val episode: Int,
    val videoId: String,
    val subTitle: String?,
    val videoDesc: String,
    val otherInfo: SwiperOtherInfo
)

data class SwiperOtherInfo(
    val region: String,
    val category: String,
    val director: String,
    val mainActors: String,
    val releaseDate: String
)
