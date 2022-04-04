package kz.nrgteam.leetread.dto.kitaphana

sealed class WeeklyProgress {
    object TRUE : WeeklyProgress()
    object FALSE : WeeklyProgress()
    object TODAY : WeeklyProgress()
    object FUTURE : WeeklyProgress()
}