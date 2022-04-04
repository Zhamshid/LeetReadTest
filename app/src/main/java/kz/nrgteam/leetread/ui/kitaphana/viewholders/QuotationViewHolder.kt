package kz.nrgteam.leetread.ui.kitaphana.viewholders

import kz.nrgteam.leetread.databinding.ItemQuotationBinding
import kz.nrgteam.leetread.dto.kitaphana.Quote
import android.util.Log
import androidx.recyclerview.widget.RecyclerView

class QuotationViewHolder(val binding: ItemQuotationBinding, onClickItem: (Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            Log.i("showKitapAdapter", absoluteAdapterPosition.toString())
            onClickItem(absoluteAdapterPosition)
        }
    }

    fun bind(item: Quote) {
        binding.title.text = item.highlighted_text
    }
}