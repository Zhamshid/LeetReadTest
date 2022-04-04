package kz.nrgteam.leetread.ui.oqu_progress

import kz.nrgteam.leetread.ui.daily_progress.DailyProgressItem
import kz.nrgteam.leetread.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.apache.commons.lang3.time.DateUtils
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OquProgressViewModel @Inject constructor(

) : BaseViewModel() {
    var currentCalendar: Calendar = Calendar.getInstance()
    var currentDayPosition = 0

    fun dailyProgressList(): List<DailyProgressItem> {
        val dates = ArrayList<DailyProgressItem>()
        var calendar = currentCalendar.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        while (dates.size < maxDaysInMonth) {
            if (DateUtils.isSameDay(currentCalendar, calendar)){
                currentDayPosition = dates.size
            }
            dates.add(DailyProgressItem("", calendar.clone() as Calendar))
            calendar = calendar.apply { add(Calendar.DAY_OF_MONTH, 1) }
        }
        return dates
    }
}