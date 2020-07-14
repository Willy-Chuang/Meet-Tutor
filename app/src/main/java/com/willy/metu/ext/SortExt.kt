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


fun List<User>?.sortByTraits(answers:Answers) : List<User> {
    return this?.filter {
        it?.let {
            if(answers.gender == ""){
            it.city == answers.city  && it.tag.contains(answers.subject)
        } else{
            it.city == answers.city && it.gender == answers.gender && it.tag.contains(answers.subject)
        }
//            it.tag.contains(answers.subject)

        }
    }
            ?: listOf()
}