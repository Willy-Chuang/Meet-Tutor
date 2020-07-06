package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedEvent(
    var id: String = "",
    var tag: String = "",
    var createdTime: Long = -1,
    var title: String = "",
    var description: String = "",
    var attendees: List<String> = listOf(""),
    val eventTime: Long = -1
) : Parcelable