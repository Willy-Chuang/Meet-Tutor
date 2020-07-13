package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        var id: String = "",
        var name: String = "",
        var email: String = "",
        var image: String = "",
        var city: String = "",
        var district: String = "",
        var gender: String = "",
        var identity: String= "",
        var tag: List<String> = listOf(""),
        var introduction: String = ""
): Parcelable