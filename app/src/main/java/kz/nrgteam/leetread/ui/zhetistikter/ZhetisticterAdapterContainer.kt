package kz.nrgteam.leetread.ui.zhetistikter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.SpacingItemDecorator
import kz.nrgteam.leetread.databinding.LayoutZhetistikterContainerBinding
import kz.nrgteam.leetread.ui.profile.adapter.ZhetistikterAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ZhetistikterAdapterContainer(val zhetistikterAdapter: ZhetistikterAdapter) :
    RecyclerView.Adapter<ZhetistikterAdapterContainer.ZhetistikterContainerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ZhetistikterContainerViewHolder {
        val binding =
            LayoutZhetistikterContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ZhetistikterContainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZhetistikterContainerViewHolder, position: Int) {
        holder.bind()
    }

    inner class ZhetistikterContainerViewHolder(private val binding: LayoutZhetistikterContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            zhetistikterAdapter.isGridLayoutManager = true
            binding.zhetistikterRv.apply {
                layoutManager =
                    GridLayoutManager(itemView.context, 3, GridLayoutManager.VERTICAL, false)
                adapter = zhetistikterAdapter
                addItemDecoration(SpacingItemDecorator(getMarginBetweenItems()))
            }
        }

        private fun getMarginBetweenItems(): Int {
            return itemView.resources.getDimension(R.dimen.layout_zhetistikter_item_margin).toInt()
        }
    }

    override fun getItemCount() = 1
}