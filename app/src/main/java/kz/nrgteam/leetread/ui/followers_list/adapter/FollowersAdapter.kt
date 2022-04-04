package kz.nrgteam.leetread.ui.followers_list.adapter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemFollowerBinding
import kz.nrgteam.leetread.dto.user.Follower
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FollowersAdapter(val listener: FollowersAdapter) :
    RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
    var followers: ArrayList<Follower> = ArrayList()

    inner class FollowersViewHolder(val binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onFollowerClicked(followers[absoluteAdapterPosition])
            }
            binding.followBtn.setOnClickListener {
                listener.onFollowClicked(
                    absoluteAdapterPosition,
                    followers[absoluteAdapterPosition]
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(currentItem: Follower, position: Int) {
            binding.readerPlace.text = (position + 1).toString()+"."
            binding.readerSchool.text = currentItem.school
            Glide.with(itemView.context)
                .load(currentItem.cover_file)
                .into(binding.readerImage)
            binding.readerNameAndSurname.text = currentItem.first_name
            if (currentItem.isInFollowing) {
                binding.followBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_profile_followed_with_backg
                    )
                )
            } else {
                binding.followBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_profile_follow_with_backg

                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val binding =
            ItemFollowerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(followers[position], position)
    }

    override fun getItemCount(): Int = followers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newValue: List<Follower>) {
        followers = newValue as ArrayList<Follower>
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun subscribedToThisFollower(position: Int) {
        followers[position] = followers[position].copy(isInFollowing = true)
        notifyItemChanged(position, 0)
    }

    fun unsubscribedToThisFollower(position: Int) {
        followers[position] = followers[position].copy(isInFollowing = false)
        notifyItemChanged(position, 0)
    }

    interface FollowersAdapter {
        fun onFollowClicked(position: Int, user: Follower)
        fun onFollowerClicked(user: Follower)
    }
}