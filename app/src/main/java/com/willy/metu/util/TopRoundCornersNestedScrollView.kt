package com.willy.metu.util

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.widget.NestedScrollView

class TopRoundCornersNestedScrollView : NestedScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outlineProvider = TopRoundCornersProvider()
        clipToOutline = true
    }

    private inner class TopRoundCornersProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            if (view.width == 0 || view.height == 0) {
                return
            }
            val radius = Utils.convertDpToPixel(20f, context)
            outline.setRoundRect(0, 0, view.width, view.height + radius.toInt(), radius)
        }
    }
}