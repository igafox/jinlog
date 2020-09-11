package com.igalogs.jinlog.data.place

import ch.hsr.geohash.GeoHash
import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Place
import java.lang.Exception

class PlaceCacheDataSource : PlaceCacheSource {

    val cacheMap = mutableMapOf<String, List<Place>?>()

    override suspend fun getPlacesByLatLong(
        latitude: Double,
        longitude: Double
    ): Result<List<Place>> {
        val geohash = calculateGeoHash(latitude, longitude)
        try {
            val data = cacheMap.getValue(geohash)
            return Result.Success(data!!)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun putPlacesByLatLng(latitude: Double, longitude: Double, data: List<Place>) {
        val geohash = calculateGeoHash(latitude, longitude)
        cacheMap[geohash] = data
    }

    override suspend fun hasCache(latitude: Double, longitude: Double): Boolean {
        val geohash = calculateGeoHash(latitude, longitude)
        return cacheMap.containsKey(geohash)
    }

    private fun calculateGeoHash(latitude: Double, longitude: Double): String {
        return GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 5)
    }
}