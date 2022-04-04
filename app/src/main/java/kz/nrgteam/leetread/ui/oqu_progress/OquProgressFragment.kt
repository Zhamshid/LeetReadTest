package kz.nrgteam.leetread.ui.oqu_progress

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentOquProgressBinding
import kz.nrgteam.leetread.ui.daily_progress.DailyProgressAdapter
import kz.nrgteam.leetread.utils.Constants
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class OquProgressFragment :
    BaseBindingFragment<FragmentOquProgressBinding>(R.layout.fragment_oqu_progress) {

    private lateinit var dailyProgressAdapter: DailyProgressAdapter
    private val viewModel: OquProgressViewModel by viewModels()

    override fun initViews(savedInstanceState: Bundle?) {
        initCalendar()
        initRV()
    }

    private fun initRV() {
        dailyProgressAdapter = DailyProgressAdapter()
        binding.dailyProgressRv.adapter = dailyProgressAdapter
        dailyProgressAdapter.items = viewModel.dailyProgressList()
        binding.dailyProgressRv.layoutManager?.scrollToPosition(viewModel.currentDayPosition)
    }

    private fun initCalendar() {
        setDateHeader(Calendar.getInstance())
        binding.calendarMonth.adapter = CustomGridCalendarAdapter(requireContext())
        binding.calendarMonth.onCalendarChangeListener = {
            onCalendarChanged(it)
        }
    }

    private fun onCalendarChanged(calendar: Calendar) {
        setDateHeader(calendar)
        if (isSameMonth(calendar, Calendar.getInstance())) {
            viewModel.currentCalendar = Calendar.getInstance()
        } else viewModel.currentCalendar = calendar
        dailyProgressAdapter.items = viewModel.dailyProgressList()
        binding.dailyProgressRv.layoutManager?.scrollToPosition(viewModel.currentDayPosition)
    }

    private fun isSameMonth(calendar: Calendar, calendar2: Calendar): Boolean {
        return calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }

    private fun setDateHeader(calendar: Calendar) {
        val calendar1 = calendar.clone() as Calendar
        val calendar2 = calendar.clone() as Calendar
        calendar1.add(Calendar.MONTH, -1)
        calendar2.add(Calendar.MONTH, 1)
        binding.calendarFirstMonth.text = makeMonthName(calendar1)
        binding.calendarSecondMonth.text = makeMonthName(calendar)
        binding.calendarThirdMonth.text = makeMonthName(calendar2)
    }

    private fun makeMonthName(calendar: Calendar): String {
        val month = DateUtils.formatDateTime(
            requireContext(), calendar.timeInMillis,
            DateUtils.FORMAT_NO_MONTH_DAY
        )
        return month.replace(
            month.split(" ")[0],
            Constants.monthsInKazakh[month.split(" ")[0]] ?: month
        )
    }

//    fun RecyclerView.smoothSnapToPosition(
//        position: Int,
//        snapMode: Int = LinearSmoothScroller.SNAP_TO_START
//    ) {
//        val smoothScroller = object : LinearSmoothScroller(this.context) {
//            override fun getVerticalSnapPreference(): Int = snapMode
//            override fun getHorizontalSnapPreference(): Int = snapMode
//        }
//        smoothScroller.targetPosition = position
//        layoutManager?.startSmoothScroll(smoothScroller)
//    }
}