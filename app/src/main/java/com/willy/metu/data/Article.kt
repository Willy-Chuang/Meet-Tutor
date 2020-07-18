package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
        var id : String = "",
        var createdTime: Long = 0,
        var creatorName: String = "",
        var creatorEmail: String = "",
        var type: String = "",
        var title: String = "",
        var city: String = "",
        var location: String = "",
        var subject: String = "",
        var detail: String = ""
): Parcelable