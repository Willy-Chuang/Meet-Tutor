package com.willy.metu.ext

import com.willy.metu.data.Event

fun List<Event>?.sortByTimeStamp (selectedTime: Long) : List<Event>{

    return this?.filter{
        it?.let {
            selectedTime <= it.eventTime && it.eventTime < selectedTime + 86400000
        }

    }
        ?: listOf()

}