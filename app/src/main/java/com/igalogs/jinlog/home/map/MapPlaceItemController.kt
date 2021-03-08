package com.igalogs.jinlog.home.map

import com.airbnb.epoxy.TypedEpoxyController
import com.igalogs.jinlog.ItemMapPlaceBindingModel_
import com.igalogs.jinlog.data.model.Place
import com.igalogs.jinlog.itemMapPlace

class MapPlaceItemController : TypedEpoxyController<List<Place>>() {

    var listener: Listener? = null

    interface Listener {

        fun onClickPlace(log: Place)

    }

    init {
        setFilterDuplicates(true)
    }

    override fun buildModels(data: List<Place>?) {
        data ?: return

       data.forEach { place ->
           itemMapPlace {
               id(modelCountBuiltSoFar)
               place(place)
               listener(listener)
           }
       }

    }

}
