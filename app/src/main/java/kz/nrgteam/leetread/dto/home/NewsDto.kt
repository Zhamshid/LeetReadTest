package kz.nrgteam.leetread.dto.home

import kz.nrgteam.leetread.model.home.News
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

data class NewsDto(
    val action_type: Int?,
    val book_id: Int?,
    val first_name: String?,
    val book_title: String?,
    val following_user_fullname: String?,
    val user_id: Int?,
    val cover_file:String?,
    val book_cover:String?,
    val following_user_id: Int?,
    val last_name: String?,
    val created_at: String?
) {
    fun toNews() = News(
        newsType = getNewsType(),
        bookId = book_id,
        firstName = first_name,
        lastName = last_name,
        followerId = user_id,
        followingId = following_user_id,
        userImage = cover_file,
        bookName = book_title,
        secondUserFullName = following_user_fullname,
        time = getTimeAsLong(),
        bookImage =book_cover
    )

    private fun getTimeAsLong(): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.getDefault())
        val a =  if (created_at.isNullOrEmpty()) System.currentTimeMillis() else (sdf.parse(created_at)
            ?: Date()).time
        Log.i("ABI", "getTimeAsLong: $a")
        return a
    }

    private fun getNewsType(): NewsType {
        return when (action_type) {
            2 -> NewsType.FINISH_BOOK
            3 -> NewsType.ADDED_BOOK
            4 -> NewsType.FOLLOWED_TO_SOMEBODY
            else -> NewsType.FOLLOWED_TO_SOMEBODY
        }
    }
}