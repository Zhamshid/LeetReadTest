package kz.nrgteam.leetread.ui.zhetistikter

import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.model.Statistic
import kz.nrgteam.leetread.model.Zhetistik
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZhetistikterViewModel @Inject constructor(
    private val repository: BaseCloudRepository
) : BaseViewModel() {

    private val statistics = listOf(
        Statistic(
            id = "0",
            title = "Осы жылғы мақсат",
            description = "100 кітап",
            goal = 10,
            currentAchievements = 5
        ),Statistic(
            id = "1",
            title = "Осы жылғы мақсат",
            description = "12 кітап",
            goal = 12,
            currentAchievements = 7
        ),Statistic(
            id = "2",
            title = "Осы жылғы мақсат",
            description = "1000 кітап",
            goal = 10,
            currentAchievements = 7
        ),Statistic(
            id = "3",
            title = "Осы жылғы мақсат",
            description = "10 кітап",
            goal = 10,
            currentAchievements = 2
        ),
    )
    val statisticsLiveData = MutableLiveData(statistics)

    private val zhetistikter = listOf(
        Zhetistik(
            id = "0",
            text = "5 кітап біріту",
            isFinished = true
        ),Zhetistik(
           id = "1",
            text = "5 кітап біріту",
            isFinished = true
        ),Zhetistik(
            id = "2",
          text =   "5 кітап біріту",
            isFinished = true

        ),Zhetistik(
            id = "3",
            text = "5 кітап біріту"
        ),Zhetistik(
            id = "4",
            text = "5 кітап біріту"
        ),Zhetistik(
            id = "5",
            text = "5 кітап біріту"
        ),Zhetistik(
            id = "6",
            text = "5 кітап біріту",
            isFinished = false

        ),Zhetistik(
            id = "7",
            text = "5 кітап біріту"
        ),Zhetistik(
            id = "8",
            text = "5 кітап біріту"
        ),Zhetistik(
            id = "9",
            text = "5 кітап біріту"
        ),Zhetistik(
            id = "10",
            text = "5 кітап біріту"
        ),
    )
    val zhetistikterLiveData = MutableLiveData(zhetistikter)


}