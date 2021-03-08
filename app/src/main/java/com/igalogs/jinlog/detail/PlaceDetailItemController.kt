package com.igalogs.jinlog.detail

import com.airbnb.epoxy.TypedEpoxyController
import com.igalogs.jinlog.ItemMapPlaceBindingModel_
import com.igalogs.jinlog.data.model.Log
import com.igalogs.jinlog.data.model.Place
import com.igalogs.jinlog.itemDetailLogEmpty
import com.igalogs.jinlog.itemHomeLog
import com.igalogs.jinlog.itemMapPlace

class PlaceDetailItemController : TypedEpoxyController<List<Log>>() {

    var listener: Listener? = null

    interface Listener {
    }

    init {
        setFilterDuplicates(true)
    }

    override fun buildModels(data: List<Log>?) {
        if(data.isNullOrEmpty()) {
            itemDetailLogEmpty {
                id(modelCountBuiltSoFar)
                spanSizeOverride { _, _, _ -> 2 }
            }
            return
        }

        data.forEach {
            itemHomeLog {
                id(modelCountBuiltSoFar)
                data(it)
            }
        }
    }

}
