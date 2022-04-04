package kz.nrgteam.leetread.ui.home.adapters

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemUserBinding
import kz.nrgteam.leetread.dto.user.UserOnline
import kz.nrgteam.leetread.utils.px
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UsersAdapter(
    private val listener: OnlineViewHolder.UserAdapterListener,
    private val marginHorizontal: Int,
    private val itemSize: Int
) :
    ListAdapter<UserOnline, OnlineViewHolder>(
        ONLINE_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineViewHolder {
        return OnlineViewHolder.create(parent, marginHorizontal, itemSize) {
            listener.onUserClicked(getItem(it).id)
        }
    }

    override fun onBindViewHolder(holder: OnlineViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val ONLINE_COMPARATOR = object : DiffUtil.ItemCallback<UserOnline>() {
            override fun areItemsTheSame(oldItem: UserOnline, newItem: UserOnline): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: UserOnline, newItem: UserOnline): Boolean =
                oldItem == newItem
        }
    }
}

class OnlineViewHolder(
    private val binding: ItemUserBinding,
    horMargin: Int,
    itemSize: Int,
    onClickUser: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onClickUser(absoluteAdapterPosition)
        }
        (itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            this.setMargins(horMargin.px(), 0, horMargin.px(), 0)
        }
        binding.innerCardView.updateLayoutParams {
            height = itemSize.px()
            width = itemSize.px()
        }
        itemView.updateLayoutParams {
            height = (itemSize + 1).px()
            width = (itemSize + 1).px()
        }
        binding.innerCardView.radius = (itemSize / 2).px().toFloat()
        binding.myCardView.radius = (itemSize / 2).px().toFloat()
    }

    fun bind(item: UserOnline) {
        if (item.isOnline) {
            binding.myCardView.setCardBackgroundColor(Color.BLUE)
        } else {
            binding.myCardView.setCardBackgroundColor(Color.TRANSPARENT)
        }
        Glide
            .with(binding.itemImage)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_person_image)
            .into(binding.itemImage)
    }

    interface UserAdapterListener {
        fun onUserClicked(id: Int)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            marginHorizontal: Int,
            itemSize: Int,
            onClickUser: (Int) -> Unit
        ): OnlineViewHolder {
            val binding =
                ItemUserBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return OnlineViewHolder(binding, marginHorizontal, itemSize, onClickUser)
        }
    }
}