package kz.nrgteam.leetread.ui.zhetistikter

import kz.nrgteam.leetread.databinding.ItemStatisticBinding
import kz.nrgteam.leetread.model.Statistic
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StatisticsAdapter(private val listener: StatisticsViewHolder.StatisticsAdapterListener) :
    ListAdapter<Statistic, StatisticsViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        return StatisticsViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Statistic>() {
            override fun areItemsTheSame(oldItem: Statistic, newItem: Statistic): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Statistic, newItem: Statistic): Boolean =
                oldItem == newItem
        }
    }
}

class StatisticsViewHolder(val binding: ItemStatisticBinding, listener: StatisticsAdapterListener) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            when (absoluteAdapterPosition) {
                0 -> {
                    listener.readDayNumberClicked()
                }
                1 -> {
                    listener.totalScoreClicked()
                }
                2 -> {
                    listener.readBookNumberClicked()
                }
                3 -> {
                    listener.readBookNumberClicked2()
                }
            }
        }
    }

    fun bind(item: Statistic) {
        binding.apply {
            progressBar.progress = item.getProgress()
            progressBarText.text = getProgressBarText(item.currentAchievements, item.goal)
            zhetistikterTitle.text = item.title
            zhetistikterDescription.text = item.description
        }
    }

    private fun getProgressBarText(current: Int, goal: Int): String {
        return "$current/$goal"
    }

    companion object {
        fun create(parent: ViewGroup, listener: StatisticsAdapterListener): StatisticsViewHolder {
            val binding =
                ItemStatisticBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return StatisticsViewHolder(binding, listener)
        }
    }

    interface StatisticsAdapterListener {
        fun readDayNumberClicked()
        fun totalScoreClicked(){}
        fun readBookNumberClicked(){}
        fun readBookNumberClicked2(){}
    }
}
