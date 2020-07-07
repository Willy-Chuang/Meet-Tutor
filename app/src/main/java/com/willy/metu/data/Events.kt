package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Events(
    var id: String = "",
    var location: String = "",
    var title: String = "",
    var description: String = "",
    var attendees: List<String> = listOf(""),
    val eventTime: Long = -1,
    var startTime: Long = -1,
    val endTime: Long = -1
) : Parcelable