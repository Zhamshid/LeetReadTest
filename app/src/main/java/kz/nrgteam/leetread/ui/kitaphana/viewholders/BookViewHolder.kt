package kz.nrgteam.leetread.ui.kitaphana.viewholders

import kz.nrgteam.leetread.databinding.ItemBookBinding
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.utils.Constants.cardCountCategory
import kz.nrgteam.leetread.utils.Constants.leftPadding
import kz.nrgteam.leetread.utils.Constants.marginBetweenCards
import kz.nrgteam.leetread.utils.ShimmerDraw
import kz.nrgteam.leetread.utils.px
import android.content.res.Resources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookViewHolder(val binding: ItemBookBinding, onClickKitap: (Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onClickKitap(absoluteAdapterPosition)
        }
    }

    fun bind(currentItem: Book) {
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
            readPagesCount.text = currentItem.pageCount.toString()
        }
        //declaring new width for each recyclerview item
        val newWidth = (Resources.getSystem().displayMetrics.widthPixels -
                (marginBetweenCards * cardCountCategory).px() -
                leftPadding.px() - marginBetweenCards.px()) / cardCountCategory

        binding.apply {
            holder.apply {
                layoutParams = layoutParams.apply {
                    width = newWidth.toInt() + 4//it's for margin
                } // changed width
            }
            card.apply {
                layoutParams =
                    layoutParams.apply {
                        width = newWidth.toInt()
                        height = (newWidth * 1.8).toInt()
                    } //changed height
            }
        }
        Glide
            .with(binding.bookImage)
            .load(currentItem.coverFile)
            .placeholder(ShimmerDraw.shimmerDrawable)
            .into(binding.bookImage)
    }
}