package com.willy.metu.data.source

import com.willy.metu.data.source.local.MeTuLocalDataSource

class DefaultMeTuRepository(
    private val remoteDataSource: MeTuDataSource,
    private val localDataSource: MeTuDataSource
) : MeTuRepository {
}