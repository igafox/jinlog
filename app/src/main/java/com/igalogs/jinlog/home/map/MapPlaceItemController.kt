package com.igalogs.jinlog.home.map

import com.airbnb.epoxy.TypedEpoxyController
import com.igalogs.jinlog.ItemMapPlaceBindingModel_
import com.igalogs.jinlog.data.model.Place
import kotlin.random.Random

class MapPlaceItemController : TypedEpoxyController<List<Place>>() {

    var listener: Listener? = null

    interface Listener {

        fun onClick(log: Place)
    }

    init {
        setFilterDuplicates(true)
    }

    override fun buildModels(data: List<Place>?) {
        data ?: return

        val item = data
            .map { item ->
                ItemMapPlaceBindingModel_()
                    .id(Random.nextLong())
                    .data(item)
            }

        this.add(item)
    }

}
