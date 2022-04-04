package kz.nrgteam.leetread.ui.all_books

import kz.nrgteam.leetread.databinding.ItemVerticalBookBinding
import kz.nrgteam.leetread.dto.kitaphana.Kitap
import kz.nrgteam.leetread.utils.ShimmerDraw
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AllBooksAdapter(
    private val listener: AllBooksAdapterListener
) : ListAdapter<Kitap, AllBooksAdapter.AllKitapViewHolder>(DIFF) {
    inner class AllKitapViewHolder(private val binding: ItemVerticalBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                getItem(absoluteAdapterPosition).id?.let { it1 -> listener.onBookClicked(it1) }
            }
        }
        fun bind(item: Kitap) {
            binding.run {
                Glide.with(root)
                    .load(item.bookImage)
                    .placeholder(ShimmerDraw.shimmerDrawable)
                    .into(image)
                title.text = item.name
                authors.text = item.authors.joinToString(", ")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllKitapViewHolder {
        val binding =
            ItemVerticalBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllKitapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllKitapViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Kitap>() {
            override fun areItemsTheSame(oldItem: Kitap, newItem: Kitap): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Kitap, newItem: Kitap): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface AllBooksAdapterListener{
        fun onBookClicked(bookId:Int)
        fun onBookTestClicked(bookId:Int){}
        fun onReadAlsoClicked(bookId:Int){}
    }

}