package kz.nrgteam.leetread.ui.profile.adapter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemZhetistikBinding
import kz.nrgteam.leetread.model.Zhetistik
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ZhetistikterAdapter(var isGridLayoutManager: Boolean = false) :
    ListAdapter<Zhetistik, ZhetistikterViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZhetistikterViewHolder {
        return ZhetistikterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ZhetistikterViewHolder, position: Int) {
        val item = getItem(position)
        if (isGridLayoutManager) {
            holder.layoutForGrid()
        }
        holder.bind(item)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Zhetistik>() {
            override fun areItemsTheSame(oldItem: Zhetistik, newItem: Zhetistik): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Zhetistik, newItem: Zhetistik): Boolean =
                oldItem == newItem
        }
    }

}

class ZhetistikterViewHolder(val binding: ItemZhetistikBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Zhetistik) {
        binding.zetistikText.text = item.text
        if (item.isFinished)
            binding.cardView.setCardBackgroundColor(getColorFinished())
        else
            binding.cardView.setCardBackgroundColor(getColorNone())
    }

    fun layoutForGrid() {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = params
    }

    private fun getColorNone(): Int {
        return ResourcesCompat.getColor(itemView.context.resources, R.color.grey_text_color, null)
    }

    private fun getColorFinished(): Int {
        return ResourcesCompat.getColor(
            itemView.context.resources,
            R.color.primaryColor,
            null
        )
    }

    companion object {
        fun create(parent: ViewGroup): ZhetistikterViewHolder {
            val binding =
                ItemZhetistikBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return ZhetistikterViewHolder(binding)
        }
    }
}