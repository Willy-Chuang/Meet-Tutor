package com.willy.metu.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.willy.metu.data.source.DefaultMeTuRepository
import com.willy.metu.data.source.MeTuDataSource
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.data.source.local.MeTuLocalDataSource
import com.willy.metu.data.source.remote.MeTuRemoteDataSource

object ServiceLocator {
    @Volatile
    var repository: MeTuRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): MeTuRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): MeTuRepository {
        return DefaultMeTuRepository(
            MeTuRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): MeTuDataSource {
        return MeTuLocalDataSource(context)
    }
}