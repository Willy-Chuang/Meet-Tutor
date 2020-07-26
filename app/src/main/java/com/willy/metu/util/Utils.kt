package com.willy.metu.util

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics


object Utils {

    fun getStatusBarHeightPixel(context: Context): Int {
        var result = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val resources: Resources = context.resources
        return resources.displayMetrics
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     * to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (getDisplayMetrics(context).densityDpi / 160f)
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     * to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return Value multiplied by the appropriate metric and truncated to
     * integer pixels.
     */
    fun convertDpToPixelSize(dp: Float, context: Context): Int {
        val pixels: Float = convertDpToPixel(dp, context)
        val res = (pixels + 0.5f).toInt()
        return when {
            res != 0 -> res
            pixels == 0f -> 0
            pixels > 0 -> 1
            else -> -1
        }
    }
}