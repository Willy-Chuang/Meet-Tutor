package com.willy.metu.ext

import com.willy.metu.data.Events

fun List<Events>?.sortByTimeStamp (selectedTime: Long) : List<Events>{

    return this?.filter{
        selectedTime < it.eventTime && it.eventTime < selectedTime + 86400000
    }
        ?: listOf()

}