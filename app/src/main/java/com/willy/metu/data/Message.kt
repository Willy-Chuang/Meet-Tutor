package com.willy.metu.data

data class Message(
        var id : String = "",
        var senderName : String = "",
        var senderImage : String = "",
        var senderEmail : String = "",
        var text : String = "",
        var createdTime : Long = 0L
)