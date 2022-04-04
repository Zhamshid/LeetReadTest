package kz.nrgteam.leetread.ui.daily_progress

import kz.nrgteam.leetread.databinding.ItemDailyProgressBinding
import kz.nrgteam.leetread.utils.Constants
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DailyProgressAdapter : RecyclerView.Adapter<DailyProgressAdapter.DailyProgressViewHolder>() {

    var items: List<DailyProgressItem> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class DailyProgressViewHolder(private val binding: ItemDailyProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyProgressItem) {
            binding.apply {
                if (DateUtils.isSameDay(item.calendar, Calendar.getInstance())) {
                    weekName.text = "Бүгін"
                } else {
                    val weekNameInEnglish =
                        item.calendar.getDisplayName(
                            Calendar.DAY_OF_WEEK,
                            Calendar.LONG,
                            Locale.ENGLISH
                        )
                    weekName.text = weekNameInEnglish!!.replace(
                        weekNameInEnglish,
                        Constants.weekNamesInKazakh[weekNameInEnglish] ?: weekNameInEnglish
                    )
                }
                val format1 = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
                date.text = format1.format(item.calendar.time)
//                progressText.text = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyProgressViewHolder {
        return DailyProgressViewHolder(
            ItemDailyProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyProgressViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

data class DailyProgressItem(val title: String, val calendar: Calendar)