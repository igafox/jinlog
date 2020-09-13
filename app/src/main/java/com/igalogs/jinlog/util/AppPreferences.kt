package com.igalogs.jinlog.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng

object AppPreferences {

    lateinit var pref : SharedPreferences

    fun init(context:Context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
    }


}