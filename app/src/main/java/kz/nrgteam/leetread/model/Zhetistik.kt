package kz.nrgteam.leetread.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Zhetistik(
    val text: String?=null,
    val id:String = "",
    val isFinished:Boolean = false
): Parcelable