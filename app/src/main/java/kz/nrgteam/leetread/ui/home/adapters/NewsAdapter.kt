package kz.nrgteam.leetread.ui.home.adapters

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemActionBinding
import kz.nrgteam.leetread.databinding.ItemActionBookBinding
import kz.nrgteam.leetread.databinding.ItemArticleBinding
import kz.nrgteam.leetread.model.Action
import kz.nrgteam.leetread.model.Article
import kz.nrgteam.leetread.utils.getSpentPublishedTime
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsAdapter(private val listener: HomeAdapterListener) :
    PagingDataAdapter<HomeChildVHUI, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeChildVHUI.ArticleUI -> {
                R.layout.item_article
            }
            is HomeChildVHUI.ActionUI -> {
                R.layout.item_action
            }
            is HomeChildVHUI.ActionBookUI -> {
                R.layout.item_action_book
            }
            null -> {
                -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_article -> {
                val binding =
                    ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ArticleViewHolder(binding) {

                }
            }
            R.layout.item_action -> {
                val binding =
                    ItemActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ActionViewHolder(binding, { pos, str ->
                    getItem(pos - 1)?.let { listener.onShareClicked(str) }
                }, {
                    listener.onUserClicked((getItem(it - 1) as HomeChildVHUI.ActionUI).action.userId)
                }, {
                    listener.onUserClicked(
                        (getItem(it - 1) as HomeChildVHUI.ActionUI).action.secondUserId ?: -1
                    )
                })
            }
            else -> {
                val binding = ItemActionBookBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ActionBookViewHolder(binding, {
                    getItem(it - 1)?.let { it1 ->
                        (it1 as HomeChildVHUI.ActionBookUI).action.text?.let { it2 ->
                            listener.onShareClicked(
                                it2
                            )
                        }
                    }
                }, {
                    listener.onUserClicked((getItem(it - 1) as HomeChildVHUI.ActionBookUI).action.userId)
                }, {
                    (getItem(it - 1) as HomeChildVHUI.ActionBookUI).action.kitap?.id?.let { it1 ->
                        listener.onBookClicked(
                            it1
                        )
                    }
                }
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postUi = getItem(position)
        postUi.let {
            when (it) {
                is HomeChildVHUI.ActionBookUI -> {
                    (holder as ActionBookViewHolder).bind(it.action)
                }
                is HomeChildVHUI.ActionUI -> {
                    (holder as ActionViewHolder).bind(it.action)
                }
                is HomeChildVHUI.ArticleUI -> {
                    (holder as ArticleViewHolder).bind(it.article)
                }
                else -> {}
            }
        }

    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<HomeChildVHUI>() {
            override fun areItemsTheSame(oldItem: HomeChildVHUI, newItem: HomeChildVHUI): Boolean {
                return (oldItem is HomeChildVHUI.ActionUI && newItem is HomeChildVHUI.ActionUI &&
                        oldItem.action.id == newItem.action.id) ||
                        (oldItem is HomeChildVHUI.ArticleUI && newItem is HomeChildVHUI.ArticleUI &&
                                oldItem.article.id == newItem.article.id) ||
                        (oldItem is HomeChildVHUI.ActionBookUI && newItem is HomeChildVHUI.ActionBookUI &&
                                oldItem.action.id == newItem.action.id)
            }


            override fun areContentsTheSame(
                oldItem: HomeChildVHUI,
                newItem: HomeChildVHUI
            ): Boolean =
                oldItem == newItem
        }
    }

    interface HomeAdapterListener {
        fun onShareClicked(text: String)
        fun onUserClicked(id: Int)
        fun onBookClicked(id: Int)
    }
}

class ActionBookViewHolder(
    private val binding: ItemActionBookBinding,
    private val onShareClicked: (Int) -> Unit,
    private val onUserClicked: (Int) -> Unit,
    private val onBookClicked: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.share.setOnClickListener {
            onShareClicked(absoluteAdapterPosition)
        }
        binding.userContainer.setOnClickListener {
            onUserClicked(absoluteAdapterPosition)
        }
        itemView.setOnClickListener {
            onBookClicked(absoluteAdapterPosition)
        }
    }

    fun bind(currentItem: Action) {
        binding.userName.text = currentItem.userName
        binding.userSpendTime.text = currentItem.published_time.getSpentPublishedTime()
        binding.likeCount.text = currentItem.likes.toString()
        binding.bookName.text = currentItem.kitap?.name ?: "Кітап аты жоқ"
        binding.bookAuthor.text =
            currentItem.kitap?.authors?.joinToString(", ") ?: "Авторлар берілмеген"
        binding.rating.isVisible = false
//        binding.rating.rating = 3f
//        binding.votedCount.text = makeVotedCountText(currentItem.kitap?.voted)
        binding.like.setOnClickListener { onLikeClicked(it) }
        Glide.with(binding.bookImage)
            .load(currentItem.kitap?.bookImage)
            .error(R.drawable.ic_no_image)
            .into(binding.bookImage)
        Glide.with(binding.userItemImage)
            .load(currentItem.userImage)
            .error(R.drawable.ic_no_image)
            .into(binding.userItemImage)

    }

    private fun onLikeClicked(it: View) {
        val minusOrPlus = if (it.isSelected) -1 else 1
        binding.likeCount.apply {
            val textToInt = text.toString().toInt() + minusOrPlus
            text = textToInt.toString()
        }
        it.isSelected = !it.isSelected
    }

//    private fun makeVotedCountText(voted: Int?): CharSequence {
//        return "(" + voted.toString() + ")"
//    }
}

class ArticleViewHolder(
    private val binding: ItemArticleBinding,
    private val onBookClicked: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onBookClicked(absoluteAdapterPosition)
        }
    }

    fun bind(currentItem: Article) {
        Glide
            .with(binding.image)
            .load(currentItem.image)
            .into(binding.image)
        binding.title.text = currentItem.title
        binding.subTitle.text = currentItem.text
    }
}

class ActionViewHolder(
    private val binding: ItemActionBinding,
    private val onShareClicked: (Int, String) -> Unit,
    private val onUserClicked: (Int) -> Unit,
    private val onSecondUserClicked: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.share.setOnClickListener {
            onShareClicked(absoluteAdapterPosition, binding.text.text.toString())
        }
        binding.userContainer.setOnClickListener {
            onUserClicked(absoluteAdapterPosition)
        }
        itemView.setOnClickListener {
            onSecondUserClicked(absoluteAdapterPosition)
        }
    }

    fun bind(currentItem: Action) {
        Glide
            .with(binding.itemImage)
            .load(currentItem.userImage)
            .into(binding.itemImage)
        binding.name.text = currentItem.userName
        binding.text.setText(
            fromSpanned(currentItem.text),
            TextView.BufferType.SPANNABLE
        )
        binding.spendTime.text = currentItem.published_time.getSpentPublishedTime()
        binding.likeCount.text = currentItem.likes.toString()
        binding.like.setOnClickListener {
            onLikeClicked(it)
        }
    }

    private fun fromSpanned(
        firstString: String?,
        secondString: String = " қолданушысына жазылды"
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder().color(
            ContextCompat.getColor(
                binding.root.context,
                R.color.primaryColor
            )
        ) {
            bold { append(firstString) }
        }.color(
            ContextCompat.getColor(
                binding.root.context,
                R.color.text_color
            )
        ) {
            append(secondString)
        }
        return builder
    }

    private fun onLikeClicked(it: View) {
        val minusOrPlus = if (it.isSelected) -1 else 1
        binding.likeCount.apply {
            val textToInt = text.toString().toInt() + minusOrPlus
            text = textToInt.toString()
        }
        it.isSelected = !it.isSelected
    }
}

sealed class HomeChildVHUI {
    data class ActionUI(val action: Action) : HomeChildVHUI()
    data class ActionBookUI(val action: Action) : HomeChildVHUI()
    data class ArticleUI(val article: Article) : HomeChildVHUI()
}