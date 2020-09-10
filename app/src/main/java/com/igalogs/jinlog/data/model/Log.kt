package com.igalogs.jinlog.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * Created by iga on 2018/04/09.
 */

data class Log(
    var id:String = "",
    var userId:String = "",
    var userName:String = "",
    var comment:String = "",
    var imagePaths:List<String> = listOf(),
    var placeName:String = "",
    var placeState:String = "",
    var placeCity:String = "",
    var placeStreet:String = "",
    var userIconPath:String = "",
    var createdDate:Date? = null,
    var updatedDate:Date? = null,
    var visitedDate:Date? = null
)