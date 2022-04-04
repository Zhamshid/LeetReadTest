package kz.nrgteam.leetread.ui.zharystarList.adapter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemReadersBinding
import kz.nrgteam.leetread.dto.user.Follower
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ReadersListAdapter(
    val isRatingFragment: Boolean, private val userId: Int, val onItemClicked: (Follower) -> Unit,
    val onUserPositionDefined: (Follower, Int) -> Unit
) :
    RecyclerView.Adapter<ReadersListAdapter.ReaderViewHolder>() {
    var readers: List<Follower> = ArrayList()
    var myPosition: Int = 0

    inner class ReaderViewHolder(val binding: ItemReadersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClicked(readers[absoluteAdapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(currentItem: Follower, position: Int) {
            val colorWhite = ContextCompat.getColor(binding.root.context, R.color.white)
            val colorText = ContextCompat.getColor(binding.root.context, R.color.text_color)
            val colorSecondText =
                ContextCompat.getColor(binding.root.context, R.color.second_text_color)
            val colorBlue = ContextCompat.getColor(binding.root.context, R.color.primaryColor)

            if (userId == currentItem.user_id) {
                onUserPositionDefined(readers[position], position)
                myPosition = position
                binding.apply {
                    readerSchool.setTextColor(colorWhite)
                    readerScore.setTextColor(colorWhite)
                    readerPlace.setTextColor(colorWhite)
                    readerNameAndSurname.setTextColor(colorWhite)
                    root.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.primaryColor
                        )
                    )
                }
            } else {
                binding.apply {
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
                }
            }
            if (position in 0..2) {
                binding.apply {
                    readerSchool.setTextColor(colorWhite)
                    readerPlace.setTextColor(colorWhite)
                    readerNameAndSurname.setTextColor(colorWhite)

                }
            }
            when (position) {
                0 -> {
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.first_place_color_yellow
                        )
                    )
                }
                1 -> {
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.second_place_color_grey
                        )
                    )
                }
                2 -> {
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.third_place_color_brown
                        )
                    )
                }
            }


            binding.apply {
                readerScore.text = (currentItem.score?:0).toInt().toString()
                readerSchool.isVisible = isRatingFragment
                if (isRatingFragment) {
                    readerSchool.text = "Бітірген кітап саны ${currentItem.finished_book_count}"
                } else {
                    readerSchool.text = currentItem.school
                }
                readerNameAndSurname.text = "${currentItem.first_name} ${currentItem.last_name}"
                readerPlace.text = "${position + 1}. "
                Glide.with(root.context)
                    .load(currentItem.cover_file)
                    .error(R.drawable.ic_no_image)
                    .into(readerImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReaderViewHolder {
        val binding = ItemReadersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReaderViewHolder, position: Int) {
        val item = readers[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = readers.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newReaders: List<Follower>) {
        readers = newReaders
        notifyDataSetChanged()
    }

}