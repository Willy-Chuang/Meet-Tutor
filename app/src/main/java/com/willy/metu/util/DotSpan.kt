package com.willy.metu.util

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class DotSpan : LineBackgroundSpan {
    private var radius: Float
    private var color: Int

    /**
     * Create a span to draw a dot using default radius and color
     *
     * @see .DotSpan
     * @see .DEFAULT_RADIUS
     */
    constructor() {
        radius = DEFAULT_RADIUS
        color = 0
    }

    /**
     * Create a span to draw a dot using a specified color
     *
     * @param color color of the dot
     * @see .DotSpan
     * @see .DEFAULT_RADIUS
     */
    constructor(color: Int) {
        radius = DEFAULT_RADIUS
        this.color = color
    }

    /**
     * Create a span to draw a dot using a specified radius
     *
     * @param radius radius for the dot
     * @see .DotSpan
     */
    constructor(radius: Float) {
        this.radius = radius
        color = 0
    }

    /**
     * Create a span to draw a dot using a specified radius and color
     *
     * @param radius radius for the dot
     * @param color color of the dot
     */
    fun dotSpan(radius: Float, color: Int) {
        this.radius = radius
        this.color = color
    }

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        charSequence: CharSequence,
        start: Int, end: Int, lineNum: Int
    ) {
        val oldColor = paint.color
        if (color != 0) {
            paint.color = color
        }
        canvas.drawCircle((left + right) / 2.toFloat(), bottom + radius, radius, paint)
        paint.color = oldColor
    }

    companion object {
        /**
         * Default radius used
         */
        const val DEFAULT_RADIUS = 3f
    }
}