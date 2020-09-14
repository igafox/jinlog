package com.igalogs.jinlog.util

object StreetViewUrl {

    @JvmStatic
    fun get(latitude:Double, longitude:Double) : String {
        return "https://maps.googleapis.com/maps/api/streetview?size=500x500&location=$latitude,$longitude&key=AIzaSyAJw3Ty6n80W80uJyQFo8aeqxX-wyRBsBg"
    }

}