package kz.nrgteam.leetread.ui.kitaphana.viewholders

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemWeeklyProgressBinding
import kz.nrgteam.leetread.dto.kitaphana.WeeklyProgress
import kz.nrgteam.leetread.utils.Constants.leftPadding
import kz.nrgteam.leetread.utils.Constants.marginBetweenCards
import kz.nrgteam.leetread.utils.px
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WeeklyProgressViewHolder(val binding: ItemWeeklyProgressBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(currentItem: WeeklyProgress) {
        val newWidth = (Resources.getSystem().displayMetrics.widthPixels -
                (marginBetweenCards * 7).px() -
                leftPadding.px() - marginBetweenCards.px()) / 7
        binding.root.apply {
            layoutParams = layoutParams.apply {
                width = newWidth
            }
            when (currentItem) {
                WeeklyProgress.FALSE -> {
                    setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
                }
                WeeklyProgress.FUTURE -> {
                    setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                }
                WeeklyProgress.TODAY -> {
                    background = ContextCompat.getDrawable(context, R.drawable.round_border_little)
                    setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_itself))

                }
                WeeklyProgress.TRUE -> {
                    setCardBackgroundColor(ContextCompat.getColor(context, R.color.primaryColor))
                }
            }
//                binding.root.setBackgroundColor(
//                    color
//                )
//                binding.innerCardView.setBackgroundColor(color)

        }
        //declaring new width for each recyclerview item
    }
}