package com.igalogs.jinlog.di

import com.igalogs.jinlog.data.log.LogDataRepository
import com.igalogs.jinlog.data.log.LogDataSource
import com.igalogs.jinlog.data.log.LogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Provides
    fun provideLogRepository(logDataSource: LogDataSource): LogRepository {
        return LogDataRepository(logDataSource)
    }

}