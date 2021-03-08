package com.igalogs.jinlog.data.place

import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Log
import com.igalogs.jinlog.data.model.Place
import java.util.*

interface PlaceDataSource {

    suspend fun getPlacesByLatLong(latitude: Double, longitude: Double): Result<List<Place>>

    suspend fun getPlace(id:String): Result<Place>

}