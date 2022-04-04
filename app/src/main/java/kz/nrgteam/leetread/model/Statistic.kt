package kz.nrgteam.leetread.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    val id: String,
    val title: String,
    val description: String,
    val goal: Int,
    val currentAchievements: Int
) : Parcelable {
    fun getProgress(): Int {
        return try {
            currentAchievements * 100 / goal
        }catch (e:Exception){
            0
        }
    }
}
