package com.igalogs.jinlog.util

object StreetViewUrl {

    const val IMAGE_HEIGHT = 480
    const val IMAGE_WIDTH = 360

    @JvmStatic
    fun get(latitude:Double, longitude:Double) : String {
        return "https://maps.googleapis.com/maps/api/streetview?size=${IMAGE_WIDTH}x${IMAGE_HEIGHT}&" +
                "location=$latitude,$longitude&key=AIzaSyAJw3Ty6n80W80uJyQFo8aeqxX-wyRBsBg"
    }

}