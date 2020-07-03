package com.willy.metu.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.DayOfWeek

class HighlightWeekendsDecorator :
    DayViewDecorator {
    private val highlightDrawable: Drawable
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val weekDay = day.date.dayOfWeek
        return weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(highlightDrawable)
    }

    companion object {
        private val color = Color.parseColor("#228BC34A")
    }

    init {
        highlightDrawable = ColorDrawable(color)
    }
}