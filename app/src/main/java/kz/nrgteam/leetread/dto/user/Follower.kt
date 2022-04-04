package kz.nrgteam.leetread.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Follower(
    var _id:Int = 0,
    var id:Int = -1,
    val first_name: String?,
    var isInFollowing: Boolean = false,
    val last_name: String?,
    val cover_file: String?,
    val is_follow:Int?,
    val school: String?,
    val user_id: Int,
    val following_id: Int,
    val score: Double?,
    val finished_book_count:String?
) : Parcelable