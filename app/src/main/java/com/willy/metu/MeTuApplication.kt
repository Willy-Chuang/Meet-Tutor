package com.willy.metu

import android.app.Application
import android.content.Context
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.util.ServiceLocator
import kotlin.properties.Delegates

class MeTuApplication : Application() {

    // Depends on the flavor,
    val meTuRepository: MeTuRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: MeTuApplication by Delegates.notNull()
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        MeTuApplication.appContext = applicationContext
        instance = this
    }

    fun isLiveDataDesign() = true
}