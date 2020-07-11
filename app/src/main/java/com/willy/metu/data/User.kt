package com.willy.metu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        var id: String = "",
        var name: String = "",
        var email: String = "",
        var image: String = ""
): Parcelable