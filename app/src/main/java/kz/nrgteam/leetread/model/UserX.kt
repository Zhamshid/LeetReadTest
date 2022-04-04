package kz.nrgteam.leetread.model

import kz.nrgteam.leetread.dto.user.Follower
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserX(
    val id: String = "0",
    val avatar: String = "",
    val name: String = "",
    val school: String = "",
    val score: Int = 0,
    val finished_book_num: Int = 0,
    val popdiski: ArrayList<Follower> = arrayListOf(),
    val podpischiki: ArrayList<Follower> = arrayListOf(),
    val rank: Int = 3,
    val continuous_reading_day_num: Int = 0,
    val finished_zhetistikter: ArrayList<Zhetistik> = arrayListOf(),
    val isInFollowing: Boolean = false
) : Parcelable
