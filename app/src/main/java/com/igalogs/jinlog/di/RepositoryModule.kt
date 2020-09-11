package com.igalogs.jinlog.di

import com.igalogs.jinlog.data.log.LogDataRepository
import com.igalogs.jinlog.data.log.LogDataSource
import com.igalogs.jinlog.data.log.LogRepository
import com.igalogs.jinlog.data.place.PlaceCacheDataSource
import com.igalogs.jinlog.data.place.PlaceDataRepository
import com.igalogs.jinlog.data.place.PlaceDataSource
import com.igalogs.jinlog.data.place.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Provides
    fun provideLogRepository(dataSource: LogDataSource): LogRepository {
        return LogDataRepository(dataSource)
    }

    @Provides
    fun providePlaceRepository(dataSource: PlaceDataSource): PlaceRepository {
        return PlaceDataRepository(dataSource, PlaceCacheDataSource())
    }

}