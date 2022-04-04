package kz.nrgteam.leetread.ui.search_user

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemReadersBinding
import kz.nrgteam.leetread.dto.user.Follower
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchedUsersAdapter(private val onUserClicked: (id: Int) -> Unit) :
    PagingDataAdapter<Follower, SearchedUsersAdapter.SearchedUserViewHolder>(COMPARATOR) {

    inner class SearchedUserViewHolder(private val binding: ItemReadersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                getItem(absoluteAdapterPosition)?.id?.let { onUserClicked(it) }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Follower) {
            val colorText = ContextCompat.getColor(binding.root.context, R.color.text_color)
            val colorSecondText =
                ContextCompat.getColor(binding.root.context, R.color.second_text_color)
            val colorBlue = ContextCompat.getColor(binding.root.context, R.color.primaryColor)

            binding.run {
                readerScore.text = (item.score ?: 0).toInt().toString()
                readerSchool.text = item.school
                readerNameAndSurname.text = "${item.first_name} ${item.last_name}"
                readerPlace.text = "${absoluteAdapterPosition + 1}. "
                readerSchool.setTextColor(colorSecondText)
                readerScore.setTextColor(colorBlue)
                readerPlace.setTextColor(colorText)
                readerNameAndSurname.setTextColor(colorText)
                root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.color_itself
                    )
                )
                Glide.with(root.context)
                    .load(item.cover_file)
                    .error(R.drawable.ic_no_image)
                    .into(readerImage)
            }
        }
    }


    override fun onBindViewHolder(holder: SearchedUserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedUserViewHolder {
        val binding = ItemReadersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchedUserViewHolder(binding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Follower>() {
            override fun areItemsTheSame(oldItem: Follower, newItem: Follower): Boolean {
                return oldItem.user_id == newItem.user_id
            }

            override fun areContentsTheSame(oldItem: Follower, newItem: Follower): Boolean =
                oldItem == newItem
        }
    }

}