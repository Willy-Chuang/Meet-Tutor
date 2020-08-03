package com.willy.metu.ext


import com.willy.metu.data.Answers
import com.willy.metu.data.Article
import com.willy.metu.data.Event
import com.willy.metu.data.User
import com.willy.metu.login.UserManager

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
            it.city == answers.city  && it.tag.contains(answers.subject) && it.email != UserManager.user.email
        } else{
            it.city == answers.city && it.gender == answers.gender && it.tag.contains(answers.subject) && it.email != UserManager.user.email
        }

        }
    }
            ?: listOf()
}

fun List<User>?.sortByName(name:String) : List<User> {
    return this?.filter {
        it.name.contains(name)
    }
            ?: listOf()
}

fun List<User>?.excludeUser() : List<User> {
    return this?.filter {
        it?.let {
            it.email != UserManager.user.email
        }
    }
            ?: listOf()
}

fun List<User>?.sortToOnlyStudents(): List<User>{
    return this?.filter {
        it?.let{
            it.identity == "Student"
        }
    }
            ?: listOf()
}

fun List<User>?.sortToOnlyTutors(): List<User>{
    return this?.filter {
        it?.let{
            it.identity == "Tutor"
        }
    }
            ?: listOf()
}

fun List<Article>?.sortByType(type: String) : List<Article>{
    return this?.filter {
        it?.let {
            it.type == type
        }
    }
            ?: listOf()
}

fun List<Article>?.sortArticleBySubject(subject: String) : List<Article>{
    return this?.filter {
        it?.let {
            it.subject == subject
        }
    }
            ?: listOf()
}

fun List<Article>?.sortArticleToMyArticle(userEmail: String) : List<Article>{
    return this?.filter {
        it?.let {
            it.creatorEmail == userEmail
        }
    }
            ?: listOf()
}

fun List<User>?.sortUserBySubject(subject: String) : List<User>{
    return  this?.filter {
        it?.let {
            it.tag.contains(subject)
        }
    }
            ?: listOf()
}