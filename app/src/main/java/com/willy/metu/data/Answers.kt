package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answers(
        var category: String = "",
        var subject: String = "",
        var city: String = "",
        var district: String = "",
        val gender: String =""
): Parcelable