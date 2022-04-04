package kz.nrgteam.leetread.ui.kitaphana.adapters

import kz.nrgteam.leetread.databinding.ItemParentKitaphanaBinding
import kz.nrgteam.leetread.ui.kitaphana.KitaphanaParentUI
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ParentKitaptarAdapter(
    val listener: ChildKitaptarAdapter.KitaphanaAdapter,
    val seeAllBooksClicked: () -> Unit
) : RecyclerView.Adapter<ParentKitaptarAdapter.ViewHolder>() {

    var items: List<KitaphanaParentUI> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(
        val binding: ItemParentKitaphanaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.seeAll.setOnClickListener {
                val item = items[absoluteAdapterPosition]
                if (item is KitaphanaParentUI.AllBooksUI) {
                    seeAllBooksClicked()
                }
            }
        }

        fun bind(currentItem: KitaphanaParentUI) {
            binding.apply {
                category.isVisible = currentItem !is KitaphanaParentUI.WeeklyProgressUI
                seeAll.isVisible = currentItem is KitaphanaParentUI.AllBooksUI
                when (currentItem) {
                    is KitaphanaParentUI.WeeklyProgressUI -> {
                        val childAdapter =
                            ChildKitaptarAdapter(listener).apply { items = currentItem.value }
                        childRV.apply {
                            layoutManager =
                                object : LinearLayoutManager(context, HORIZONTAL, false) {
                                    override fun canScrollHorizontally(): Boolean {
                                        return false
                                    }
                                }
                            adapter = childAdapter
                        }
                    }
                    is KitaphanaParentUI.BooksUI -> {
                        val childAdapter =
                            ChildKitaptarAdapter(listener).apply { items = currentItem.value }
                        childRV.apply {
                            adapter = childAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    root.context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                        category.text = "Кітаптарым"
                    }
                    is KitaphanaParentUI.EditorChoiceUI -> {
                        val childAdapter =
                            ChildKitaptarAdapter(listener).apply { items = currentItem.value }
                        childRV.apply {
                            adapter = childAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    root.context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                        category.text = "Редацияның таңдауы"
                    }
                    is KitaphanaParentUI.LastOpenedUI -> {
                        val childAdapter =
                            ChildKitaptarAdapter(listener).apply { items = currentItem.value }
                        childRV.apply {
                            adapter = childAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    root.context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                        category.text = "Соңғы ашылғандар"
                    }
                    is KitaphanaParentUI.QuoteUI -> {
                        val childAdapter =
                            ChildKitaptarAdapter(listener).apply { items = currentItem.value }
                        childRV.apply {
                            adapter = childAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    root.context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                        category.text = "Менің таңдаулы цитаталарым"
                    }
                    is KitaphanaParentUI.AllBooksUI -> {
                        val childAdapter =
                            ChildKitaptarAdapter(listener).apply { items = currentItem.value }
                        childRV.apply {
                            adapter = childAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    root.context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                        category.text = "Барлық кітаптар"
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemParentKitaphanaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = items.size

}