package kz.nrgteam.leetread.ui.login.typeOfUser

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.ItemTypeOfUserBinding
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TypeOfUserAdapter (
    private val listener: TypeUserAdapterListener
        ): RecyclerView.Adapter<TypeOfUserAdapter.TypeOfUserViewHolder>() {

    var items:List<TypeOfUser> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    set(value){
        field = value
        notifyDataSetChanged()
    }

    var selectedItemPos = 0
    var lastItemSelectedPos = 0

    inner class TypeOfUserViewHolder(private val binding: ItemTypeOfUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val primaryColor =
            ContextCompat.getColor(binding.root.context, R.color.primary_light_selected)
        private val primaryColorLight =
            ContextCompat.getColor(binding.root.context, R.color.white)
        private val defaultTextColor =
            ContextCompat.getColor(binding.root.context, R.color.primaryColor)
        private val selectedTextColor =
            ContextCompat.getColor(binding.root.context, R.color.primaryColor)
        init {
            itemView.setOnClickListener {
                listener.onClickType(absoluteAdapterPosition)
                changeSelectedPosition()
            }
        }
        fun bind(item:TypeOfUser){
            binding.run {
                title.text = item.title
            }
        }
        private fun changeSelectedPosition() {
            selectedItemPos = absoluteAdapterPosition
            when {
                lastItemSelectedPos != -1 && lastItemSelectedPos != selectedItemPos -> {
                    notifyItemChanged(lastItemSelectedPos, 2)
                }
            }
            notifyItemChanged(selectedItemPos, 0)
        }

        fun defaultCardStroke(ownBinding: ItemTypeOfUserBinding = binding) {
            ownBinding.title.setTextColor(defaultTextColor)
            ownBinding.root.setCardBackgroundColor(primaryColorLight)
        }

        fun selectedCardStroke() {
            binding.title.setTextColor(selectedTextColor)
            binding.root.setCardBackgroundColor(primaryColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeOfUserViewHolder {
        val binding = ItemTypeOfUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeOfUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeOfUserViewHolder, position: Int) {
        val item = items[position]
        if (item.isSelected) {
            holder.selectedCardStroke()
            lastItemSelectedPos = position
        } else
            holder.defaultCardStroke()
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: TypeOfUserViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            when (payloads[0]) {
                2 -> {
                    items[position].isSelected = false
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount() = items.size

    interface TypeUserAdapterListener{
        fun onClickType(pos:Int)
    }

}
