package kz.nrgteam.leetread.ui.calendarviewpager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import org.apache.commons.lang3.time.DateUtils
import java.util.*

open class CalendarPagerAdapter(val context: Context, base: Calendar = Calendar.getInstance(),
                                val startingAt: DayOfWeek = DayOfWeek.Monday
) : PagerAdapter() {
    private val baseCalendar: Calendar = DateUtils.truncate(base, Calendar.DAY_OF_MONTH).apply {
        set(Calendar.DAY_OF_MONTH, 1)
        firstDayOfWeek = Calendar.SUNDAY + startingAt.getDifference()
        minimalDaysInFirstWeek = 1
    }

    private var viewContainer: ViewGroup? = null

    var selectedDay: Date? = null
        set(value) {
            field = value
            notifyCalendarItemChanged()
        }
//    var onDayClickLister: ((Day, Int) -> Unit)? = null
//    var onDayLongClickListener: ((Day) -> Boolean)? = null

    companion object {
        const val MAX_VALUE = 100
    }

    override fun getCount(): Int = MAX_VALUE
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recyclerView = RecyclerView(context).apply {
            layoutManager = GridLayoutManager(context, 7)
            isNestedScrollingEnabled = false
            overScrollMode = View.OVER_SCROLL_NEVER
            hasFixedSize()
            adapter = object : CalendarCellAdapter(context, getCalendar(position),
                startingAt, selectedDay) {
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, day: Day) {
//                    holder.itemView.setOnClickListener {
////                        if(day.state!=DayState.ThisMonth){
////                            viewPager.moveItemBy()
////                        }
//                        this@CalendarPagerAdapter.selectedDay = day.calendar.time
//                        this@CalendarPagerAdapter.onDayClickLister?.invoke(day, position - MAX_VALUE / 2)
//                        notifyCalendarItemChanged()
//                    }
//                    holder.itemView.setOnLongClickListener {
//                        if (this@CalendarPagerAdapter.onDayLongClickListener != null) {
//                            this@CalendarPagerAdapter.onDayLongClickListener!!.invoke(day)
//                        } else {
//                            false
//                        }
//                    }
                    this@CalendarPagerAdapter.onBindView(holder.itemView, day)
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                        object : RecyclerView.ViewHolder(this@CalendarPagerAdapter
                            .onCreateView(parent, viewType)) {}

            }
        }
        container.addView(recyclerView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT))
        viewContainer = container

        return recyclerView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)

    fun getCalendar(position: Int): Calendar {

        return (baseCalendar.clone() as Calendar).apply {
            add(Calendar.MONTH, position - MAX_VALUE / 2)
        }
    }

    fun notifyCalendarChanged() {
        val views = viewContainer ?: return
        (0 until views.childCount).forEach { i ->
            ((views.getChildAt(i) as? RecyclerView)?.adapter as? CalendarCellAdapter)?.run {
                notifyItemRangeChanged(0, items.size)
            }
        }
    }

    private fun notifyCalendarItemChanged() {
        val views = viewContainer ?: return
        (0 until views.childCount).forEach { i ->
            ((views.getChildAt(i) as? RecyclerView)?.adapter as? CalendarCellAdapter)
                ?.updateItems(selectedDay)
        }
    }

    open fun onCreateView(parent: ViewGroup, viewType: Int): View {
        return TextView(context)
    }

    open fun onBindView(view: View, day: Day) {

    }

    enum class DayOfWeek {
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday;

        fun getDifference(): Int {
            return when (this) {
                Sunday -> 0
                Monday -> 1
                Tuesday -> 2
                Wednesday -> 3
                Thursday -> 4
                Friday -> 5
                Saturday -> 6
            }
        }
        fun isLessFirstWeek(calendar: Calendar): Boolean {
            return calendar.get(Calendar.DAY_OF_WEEK) < getDifference() + 1
        }
        fun isMoreLastWeek(calendar: Calendar): Boolean {
            val end = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH)
            end.add(Calendar.MONTH, 1)
            end.add(Calendar.DATE, -1)
            return end.get(Calendar.DAY_OF_WEEK) < getDifference() + 1
        }
    }
}
