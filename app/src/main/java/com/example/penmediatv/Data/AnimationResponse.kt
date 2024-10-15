package com.example.penmediatv.Data

data class AnimationResponse(
    val code: String,
    val message: String,
    val data: AnimationData
)

data class AnimationData(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Int,
    val records: List<AnimationItem>
)

data class AnimationItem(
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val episode: Int,
    val videoId: String,
    val subTitle: String?,
    val videoDesc: String,
    val otherInfo: AnimationOtherInfo
)

data class AnimationOtherInfo(
    val region: String,
    val category: String,
    val director: String,
    val mainActors: String,
    val releaseDate: String
)

