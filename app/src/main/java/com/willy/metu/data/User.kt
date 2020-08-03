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
        var tag: List<String> = listOf(),
        var introduction: String = "",
        var experience: String = "",
        var joinedTime: Long = 0,
        var followingEmail: List<String> = listOf(),
        var followingName: List<String> = listOf(),
        var followedBy: List<String> = listOf()
): Parcelable