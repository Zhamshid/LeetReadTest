package kz.nrgteam.leetread.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val nickname: String?,
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val aboutMe: String?,
    val schoolName: String?,
    val grade: String?,
    val imageUrl: String?,
    val annualGoalNumber:Int?,
    val finishedBooksNumber:Int?
) : Parcelable
