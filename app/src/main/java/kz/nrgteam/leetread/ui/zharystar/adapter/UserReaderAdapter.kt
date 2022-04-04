package kz.nrgteam.leetread.ui.zharystar.adapter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemUserReaderBinding
import kz.nrgteam.leetread.model.UserX
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserReaderAdapter : ListAdapter<UserX, UserViewHolder>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<UserX>() {
            override fun areItemsTheSame(oldItem: UserX, newItem: UserX): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: UserX, newItem: UserX): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class UserViewHolder(val binding: ItemUserReaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(currentItem: UserX, position: Int) {
        Glide.with(itemView.context)
            .load(currentItem.avatar)
            .into(binding.userImage)
        val marginValue = getTranslationFloatForItems(position)
        binding.root.translationX = marginValue
    }

    private fun getTranslationFloatForItems(position: Int): Float {
        return -itemView.context.resources.getDimension(
            R.dimen.zharystar_user_reader_image_height_width
        ) / 1.8f * position
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val binding =
                ItemUserReaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return UserViewHolder(binding)
        }
    }
}