package kz.nrgteam.leetread.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(padding: Int) : RecyclerView.ItemDecoration() {

    constructor(left:Int, right:Int, top:Int, bottom:Int):this(top){
        this.bottom = bottom
        this.left = left
        this.right= right
        this.top= top
    }
    private var left = padding
    private var right = padding
    private var top = padding
    private var bottom = padding

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = top
        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right
    }
}