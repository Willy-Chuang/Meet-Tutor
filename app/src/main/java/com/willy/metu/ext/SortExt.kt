package com.willy.metu.ext

import com.willy.metu.data.Answers
import com.willy.metu.data.Event
import com.willy.metu.data.User

fun List<Event>?.sortByTimeStamp (selectedTime: Long) : List<Event>{

    return this?.filter{
        it?.let {
            selectedTime <= it.eventTime && it.eventTime < selectedTime + 86400000
        }

    }
        ?: listOf()

}


fun List<User>?.filterByTraits(answers:Answers) : List<User> {
    return this?.filter {
        it?.let {
            it.city == answers.city && it.gender == answers.gender && it.tag.contains(answers.subject)
        }
    }
            ?: listOf()
}