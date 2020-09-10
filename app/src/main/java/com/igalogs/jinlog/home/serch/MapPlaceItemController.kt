package com.igalogs.jinlog.home.serch

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.igalogs.jinlog.ItemHomeHeaderBindingModel_
import com.igalogs.jinlog.ItemHomeLogBindingModel_
import com.igalogs.jinlog.ItemMapPlaceBindingModel_
import com.igalogs.jinlog.data.model.Log
import com.igalogs.jinlog.data.model.Place

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
                    .id(item.id)
                    .data(item)
            }

        this.add(item)
    }

}
