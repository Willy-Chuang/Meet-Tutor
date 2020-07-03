package com.willy.metu.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class EventDecorator(private val color: Int, dates: Collection<CalendarDay>?) :
    DayViewDecorator {
    private val dates: HashSet<CalendarDay>
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, color))
    }

    init {
        this.dates = HashSet(dates)
    }
}