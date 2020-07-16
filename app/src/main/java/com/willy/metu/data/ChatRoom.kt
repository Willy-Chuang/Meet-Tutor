package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
        var chatRoomId : String = "",
        var createdTime : Long = -0L,
        var userName: String = "",
        var attendeeNames: List<String> = listOf(""),
        var attendeeImages: List<String> = listOf(""),
        var latestMessage: String =""
): Parcelable
