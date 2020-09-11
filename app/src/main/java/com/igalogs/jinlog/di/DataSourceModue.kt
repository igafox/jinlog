package com.igalogs.jinlog.di

import com.google.firebase.firestore.FirebaseFirestore
import com.igalogs.jinlog.data.log.FirestoreLogDataSource
import com.igalogs.jinlog.data.log.LogDataSource
import com.igalogs.jinlog.data.place.FirestorePlaceDataSource
import com.igalogs.jinlog.data.place.PlaceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataSourceModue {

    @Provides
    @Singleton
    fun provideFirebaseLogDataSouce(firestore: FirebaseFirestore): LogDataSource {
        return FirestoreLogDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebasePlaceDataSouce(firestore: FirebaseFirestore): PlaceDataSource {
        return FirestorePlaceDataSource(firestore)
    }

}
