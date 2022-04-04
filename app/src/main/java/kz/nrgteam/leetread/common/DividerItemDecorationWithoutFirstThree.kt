package kz.nrgteam.leetread.common

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class DividerItemDecorationWithoutFirstThree(private val mDivider: Drawable?) : ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 2 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop: Int = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop +(mDivider?.intrinsicHeight ?:0)
            mDivider?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider?.draw(c)
        }
    }
}