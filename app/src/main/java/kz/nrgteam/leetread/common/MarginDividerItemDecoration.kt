package kz.nrgteam.leetread.common

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.utils.px
import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class MarginDividerItemDecoration(context: Context, orientation: Int, var spaceTop: Int, var spaceBottom: Int) :
    DividerItemDecoration(context, orientation) {

    init {
        val divider = ContextCompat.getDrawable(context, R.drawable.divider)
        divider?.let { setDrawable(it) }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = spaceBottom.px()
        outRect.top = spaceTop.px()
    }
}