package com.willy.metu

import android.app.Application
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.util.ServiceLocator
import kotlin.properties.Delegates

class MeTuApplication : Application() {

    // Depends on the flavor,
    val meTuRepository: MeTuRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: MeTuApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isLiveDataDesign() = true
}