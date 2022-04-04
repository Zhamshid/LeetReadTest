package kz.nrgteam.leetread.ui.kitaphana.viewholders

import kz.nrgteam.leetread.databinding.ItemEditorChoiceBinding
import kz.nrgteam.leetread.utils.ShimmerDraw
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EditorChoiceViewHolder(val binding: ItemEditorChoiceBinding, onClickItem:(Int)->Unit) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            Log.i("showKitapAdapter", absoluteAdapterPosition.toString())
            onClickItem(absoluteAdapterPosition)
        }
    }
    fun bind(item: String) {
        Glide
            .with(binding.root.context)
            .load("https://npr.brightspotcdn.com/dims4/default/297b41a/2147483647/strip/true/crop/4032x3024+0+0/resize/880x660!/quality/90/?url=http%3A%2F%2Fnpr-brightspot.s3.amazonaws.com%2F37%2F72%2F89c34c8642cf902de4f70af19ed9%2Fbanned-books.jpg")
            .placeholder(ShimmerDraw.shimmerDrawable)
            .into(binding.image)
        binding.title.text = item
    }
}