package kz.nrgteam.leetread.ui.home.adapters

import kz.nrgteam.leetread.databinding.LayoutStoriesContainerBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoriesContainerAdapter(val title: String, val usersAdapter: UsersAdapter) :
    RecyclerView.Adapter<StoriesContainerAdapter.ClubsContainerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubsContainerViewHolder {
        val binding =
            LayoutStoriesContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ClubsContainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubsContainerViewHolder, position: Int) {
        holder.bind()
    }

    inner class ClubsContainerViewHolder(private val binding: LayoutStoriesContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.storiesContainerTitle.text = title
            binding.storiesRv.apply {
                layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = usersAdapter
            }
        }
    }

    override fun getItemCount() = 1
}
