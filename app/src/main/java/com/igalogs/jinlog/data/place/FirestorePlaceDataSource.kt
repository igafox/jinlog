package com.igalogs.jinlog.data.place

import android.util.Log.e
import ch.hsr.geohash.GeoHash
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Log
import com.igalogs.jinlog.data.model.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class FirestorePlaceDataSource(
    private var firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlaceDataSource {

    val placeCollection = firestore.collection("places")

    override suspend fun getPlacesByLatLong(
        latitude: Double,
        longitude: Double
    ): Result<List<Place>> = withContext(ioDispatcher) {
        return@withContext try {

            val geohash = GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 5)

            val startHash = geohash
            val lastChar = geohash.last()
            val nextChar = lastChar + 1
            val endHash = geohash.substring(0, geohash.length - 1) + nextChar

            val query = placeCollection
                .whereGreaterThanOrEqualTo("geohash", startHash)
                .whereLessThan("geohash", endHash)

            var data = query.get(Source.DEFAULT).await().toObjects(Place::class.java)

            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}
