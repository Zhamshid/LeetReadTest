package kz.nrgteam.leetread.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class School(
    val id: Int = 0,
    val title: String = "",
    val region: String = ""
) : Parcelable