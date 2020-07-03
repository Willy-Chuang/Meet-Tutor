package com.willy.metu.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


class SingleDateDecorator(private val color: Int, date: CalendarDay) :
    DayViewDecorator {
    var theDay = date
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == theDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, color))
    }

    init {

    }
}