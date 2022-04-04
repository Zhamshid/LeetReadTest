package kz.nrgteam.leetread.dto.user

data class UserChallenge(
    val first_by_day: Int,
    val first_by_month: Int,
    val first_by_week: Int,
    val second_by_day: Int,
    val second_by_month: Int,
    val second_by_week: Int,
    val third_by_day: Int,
    val third_by_month: Int,
    val third_by_week: Int,
    val user_id: Int
)