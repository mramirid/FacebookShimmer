package com.mramirid.facebookshimmer.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class ShimmerDividerItemDecoration(
    private val context: Context?,
    private val orientation: Int,
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        private const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        private const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }

    private var divider: Drawable

    constructor() : this(null, 0, 0)

    init {
        val attr = context!!.obtainStyledAttributes(ATTRS)
        divider = attr.getDrawable(0)!!
        attr.recycle()
        setOrientation(orientation)
    }

    private fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)) { "invalid orientation" }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)

        if (orientation == VERTICAL_LIST) drawVertical(canvas, parent)
        else drawHorizontal(canvas, parent)
    }

    private fun drawVertical(canvas: Canvas?, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        parent.forEach { child ->
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left + dpToPx(margin), top, right - dpToPx(margin), bottom)
            divider.draw(canvas!!)
        }
    }

    private fun drawHorizontal(canvas: Canvas?, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        parent.forEach { child ->
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicHeight
            divider.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin))
            divider.draw(canvas!!)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        } else {
            outRect.set(0, 0, divider.intrinsicWidth, 0)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val resources = context!!.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).roundToInt()
    }
}