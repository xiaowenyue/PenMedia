package com.example.penmediatv.Data

data class HomeResponse(
    val code: String,
    val message: String,
    val data: HomeData
)

data class HomeData(
    val swiperResourceList: List<SwiperResourceItem>,
    val recommandResourceList: List<SwiperResourceItem>
)

data class SwiperResourceItem(
    val videoNameEn: String,
    val videoNameZh: String,
    val videoCover: String,
    val episode: Int,
    val duration: Int,
    val videoId: String,
    val subTitle: String?,
    val videoType: String,
    val otherInfo: AnimationOtherInfo
)