package kz.nrgteam.leetread.dto.user

data class User(
    val userAchievements: List<UserAchievement>,
    val userChallenges: List<UserChallenge>,
    val userInfo: UserInfoDto
)