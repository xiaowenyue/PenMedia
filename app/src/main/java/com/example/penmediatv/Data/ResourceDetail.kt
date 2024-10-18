package com.example.penmediatv.Data

data class ResourceDetailResponse(
    val code: String,
    val message: String,
    val data: ResourceDetailData
)

data class ResourceDetailData(
    val introduction: String,
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val episode: Int,
    val videoId: String,
    val videoUrl: String,
    val category: String,
    val videoType: String,
    val subTitle: String?,
    val otherInfo: AnimationOtherInfo
)