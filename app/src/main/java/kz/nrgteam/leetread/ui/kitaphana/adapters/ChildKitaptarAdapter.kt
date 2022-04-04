package kz.nrgteam.leetread.ui.kitaphana.adapters

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.*
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.dto.kitaphana.Quote
import kz.nrgteam.leetread.dto.kitaphana.WeeklyProgress
import kz.nrgteam.leetread.ui.kitaphana.viewholders.*
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ChildKitaptarAdapter(
    val listener: KitaphanaAdapter
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<BookVHUI> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.item_book -> {
                val binding =
                    ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return BookViewHolder(binding){
                    listener.onClickKitap((items[it] as BookVHUI.VHBook).book)
                }
            }
            R.layout.item_book_last_opened -> {
                val binding = ItemBookLastOpenedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return LastOpenedViewHolder(binding){
                    listener.onClickKitap((items[it] as BookVHUI.VHLastOpenedBook).book)
                }
            }
            R.layout.item_editor_choice -> {
                val binding = ItemEditorChoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return EditorChoiceViewHolder(binding){
                    listener.onClickEditorChoice((items[it] as BookVHUI.VHEditorChoice).choice)
                }
            }
            R.layout.item_weekly_progress -> {
                val binding = ItemWeeklyProgressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return WeeklyProgressViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemQuotationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return QuotationViewHolder(binding){
                    listener.onClickQuote((items[it] as BookVHUI.VHQuotation).quotation)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            is BookVHUI.VHEditorChoice -> (holder as EditorChoiceViewHolder).bind(currentItem.choice)
            is BookVHUI.VHLastOpenedBook -> (holder as LastOpenedViewHolder).bind(currentItem.book)
            is BookVHUI.VHQuotation -> (holder as QuotationViewHolder).bind(currentItem.quotation)
            is BookVHUI.VHWeeklyProgress -> (holder as WeeklyProgressViewHolder).bind(currentItem.color)
            is BookVHUI.VHBook -> (holder as BookViewHolder).bind(currentItem.book)
            is BookVHUI.VHAllBook -> (holder as BookViewHolder).bind(currentItem.book)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is BookVHUI.VHEditorChoice -> {
                R.layout.item_editor_choice
            }
            is BookVHUI.VHLastOpenedBook -> {
                R.layout.item_book_last_opened
            }
            is BookVHUI.VHQuotation -> {
                R.layout.item_quotation
            }
            is BookVHUI.VHWeeklyProgress -> {
                R.layout.item_weekly_progress
            }
            else -> {
                R.layout.item_book
            }
        }
    }

    interface KitaphanaAdapter {
        fun onClickKitap(kitap: Book)
        fun onClickEditorChoice(id: String)
        fun onClickQuote(quote: Quote)
    }
}

sealed class BookVHUI {
    class VHLastOpenedBook(val book: Book) : BookVHUI()
    class VHBook(val book: Book) : BookVHUI()
    class VHAllBook(val book: Book) : BookVHUI()
    class VHWeeklyProgress(val color: WeeklyProgress) : BookVHUI()
    class VHQuotation(val quotation: Quote) : BookVHUI()
    class VHEditorChoice(val choice: String) : BookVHUI()
}
