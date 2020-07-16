package com.willy.metu.data

data class Message(
        var id : String = "",
        var senderName : String = "",
        var senderImage : String = "",
        var text : String = "",
        var createdTime : Long = 0L
)