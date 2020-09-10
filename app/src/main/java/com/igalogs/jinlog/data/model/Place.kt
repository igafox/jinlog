package com.igalogs.jinlog.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * Created by iga on 2018/04/09.
 */

data class Place(
    var id: String = "",
    var name: String = "",
    var state: String = "",
    var city: String = "",
    var street: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var location: GeoPoint? = null,
    var geohash: String = ""
)
