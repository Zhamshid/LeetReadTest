package kz.nrgteam.leetread.ui.kitaphana.viewholders

import kz.nrgteam.leetread.databinding.ItemBookLastOpenedBinding
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.utils.Constants.cardCountOnLastOpenedCategory
import kz.nrgteam.leetread.utils.Constants.leftPadding
import kz.nrgteam.leetread.utils.Constants.marginBetweenCards
import kz.nrgteam.leetread.utils.ShimmerDraw
import kz.nrgteam.leetread.utils.px
import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

class LastOpenedViewHolder(val binding: ItemBookLastOpenedBinding, onClickKitap: (Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            Log.i("showKitapAdapter", absoluteAdapterPosition.toString())
            onClickKitap(absoluteAdapterPosition)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(currentItem: Book) {
        binding.progressbarContainer.isVisible = true
        binding.apply {
            val myProgress =
                (currentItem.page_count_progress.toFloat() / (currentItem.pageCount ?: 1))
            progressBar.apply {
                isVisible = true
                progress = (myProgress * 100).toInt()
            }
            progressCount.text = (myProgress * 100).roundToInt().toString() + "%"

        }
        val cardCount = cardCountOnLastOpenedCategory
        binding.apply {
            authorName.apply {
                if (currentItem.authors.isNullOrEmpty()) {
                    isVisible = false
                } else {
                    isVisible = true
                    text = currentItem.authors[0]
                }
            }
            bookName.apply {
                isVisible = true
                text = currentItem.title

            }
        }
        //declaring new width for each recyclerview item
        val newWidth = (Resources.getSystem().displayMetrics.widthPixels -
                (marginBetweenCards * cardCount).px() -
                leftPadding.px() - marginBetweenCards.px()) / cardCount

        binding.apply {
            holder.apply {
                layoutParams = layoutParams.apply {
                    width = newWidth.toInt() } // changed width
            }
            card.apply {
                layoutParams =
                    layoutParams.apply {
                        height = (newWidth * 1.6).toInt() } //changed height
            }
        }
        Glide
            .with(binding.bookImage)
            .load(currentItem.coverFile)
            .placeholder(ShimmerDraw.shimmerDrawable)
            .into(binding.bookImage)
    }
}