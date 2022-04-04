package kz.nrgteam.leetread.ui.oqu_progress

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.ui.calendarviewpager.CalendarPagerAdapter
import kz.nrgteam.leetread.ui.calendarviewpager.Day
import kz.nrgteam.leetread.ui.calendarviewpager.DayState
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.apache.commons.lang3.time.DateUtils
import java.util.*

class CustomGridCalendarAdapter(context: Context) :
    CalendarPagerAdapter(context) {

    private val listOfWorkedDays = listOf<Calendar>(
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -3)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -1)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -2)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -4)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -10)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -31)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -21)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -40)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -45)
//        },
//        Calendar.getInstance().apply {
//            add(Calendar.DAY_OF_MONTH, -41)
//        },
    )

    override fun onCreateView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.item_calendar_cell, parent, false)
    }

    override fun onBindView(view: View, day: Day) {
        val tv = view.findViewById<TextView>(R.id.calendar_day)
        val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)
        val sl = view.findViewById<View>(R.id.start_line)
        val el = view.findViewById<View>(R.id.end_line)
        tv.text = day.calendar.get(Calendar.DAY_OF_MONTH).toString()
        val calendar = Calendar.getInstance()
        calendar.time = day.calendar.time
        when {
            day.state != DayState.ThisMonth -> {
                tv.setTextColor(Color.GRAY)
            }
            day.state == DayState.ThisMonth && isItPreviousDay(day.calendar) && !day.isToday -> {
                tv.setTextColor(Color.RED)
            }
            else -> {
                tv.setTextColor(primaryColor)
            }
        }
        //new Logic
        val nextDay = (day.calendar.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }
        val prevDay = (day.calendar.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, -1) }
        if (isItContains(listOfWorkedDays, day.calendar)) {
            tv.setBackgroundResource(R.drawable.round_for_day_cell)
            tv.setTextColor(Color.WHITE)
            if (isItContains(
                    listOfWorkedDays,
                    nextDay
                ) && nextDay.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY
            ) {
                el.visibility = View.VISIBLE
            }
            if (isItContains(
                    listOfWorkedDays,
                    prevDay
                ) && prevDay.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
            ) {
                sl.visibility = View.VISIBLE
            }
        }

        if (day.isToday) {
            tv.setBackgroundResource(R.drawable.circular_bordershape)
        }
    }

    private fun isItContains(listOfWorkedDays: List<Calendar>, day: Calendar): Boolean {
        for (d in listOfWorkedDays) {
            if (DateUtils.isSameDay(d, day)) {
                return true
            }
        }
        return false
    }

    private fun isItPreviousDay(calendar: Calendar): Boolean {
        return Calendar.getInstance().compareTo(calendar) == 1
    }

}