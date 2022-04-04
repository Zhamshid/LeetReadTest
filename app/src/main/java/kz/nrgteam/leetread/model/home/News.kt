package kz.nrgteam.leetread.model.home

import kz.nrgteam.leetread.dto.home.NewsType

data class News(
    val newsType: NewsType,
    val bookId: Int?,
    val bookName: String?,
    val userImage: String?,
    val bookImage: String?,
    val firstName: String?,
    val lastName: String?,
    val followerId: Int?,
    val followingId: Int?,
    val time: Long = System.currentTimeMillis(),
    val secondUserFullName: String?,
)