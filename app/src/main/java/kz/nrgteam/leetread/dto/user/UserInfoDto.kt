package kz.nrgteam.leetread.dto.user

import kz.nrgteam.leetread.model.UserInfo

data class UserInfoDto(
    val city: String?,
    val first_name: String?,
    val followers: String?,
    val followings: String?,
    val grade: Int?,
    val id: Int?,
    val is_follow: String?,
    val region: String?,
    val last_name: String?,
    val cover_file: String?,
    val school: String?,
    val school_id: Int?,
    val aim: Aim?,
    val selfUser: Int?
) {
    fun toUserInfo() = UserInfo(
        nickname = "",
        fullName = "$first_name $last_name",
        schoolName = getSchoolWithRegion(),
        imageUrl = cover_file,
        email = "",
        phoneNumber = "",
        aboutMe = "",
        grade = getGradeText(),
        annualGoalNumber = aim?.books_tofinish,
        finishedBooksNumber = aim?.finished,
    )

    private fun getGradeText(): String {
        return "$grade сыныбы"
    }
    fun getSchoolWithRegion():String{
        return "$region $school"
    }
}
data class Aim(val id: String?, val user_id: String?, val books_tofinish: Int?, val finished: Int?)