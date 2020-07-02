package com.willy.metu.ext

import android.app.Activity
import com.willy.metu.MeTuApplication
import com.willy.metu.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as MeTuApplication).meTuRepository
    return ViewModelFactory(repository)
}