package com.igalogs.jinlog.data.place

import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Log
import com.igalogs.jinlog.data.model.Place
import java.util.*

class PlaceDataRepository(
    private var dataSource: PlaceDataSource,
    private var cacheSource: PlaceCacheSource
) : PlaceRepository {

    override suspend fun getPlacesByLatLong(
        latitude: Double,
        longitude: Double
    ): Result<List<Place>> {

        if (cacheSource.hasCache(latitude, longitude)) {
            return cacheSource.getPlacesByLatLong(latitude, longitude)
        } else {
            val result = dataSource.getPlacesByLatLong(latitude, longitude)
            if (result is Result.Success) {
                cacheSource.putPlacesByLatLng(latitude, longitude, result.data)
            }
            return result
        }

    }
}