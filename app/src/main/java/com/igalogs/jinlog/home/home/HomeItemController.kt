package com.igalogs.jinlog.home.home

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.igalogs.jinlog.ItemHomeHeaderBindingModel_
import com.igalogs.jinlog.ItemHomeLogBindingModel_
import com.igalogs.jinlog.data.model.Log

class HomeItemController : TypedEpoxyController<List<Log>>() {

    var listener: Listener? = null

    @AutoModel lateinit var header: ItemHomeHeaderBindingModel_

    interface Listener {

        fun onClick(log: Log)
    }

    init {
        setFilterDuplicates(true)
    }

    override fun buildModels(data: List<Log>?) {
        data ?: return

        header.spanSizeOverride { _, _, _ -> 2 }
        this.add(header)

        val item = data?.filter { item -> item.id.isNotBlank() }
            .map { item ->
                ItemHomeLogBindingModel_()
                    .id(item.id)
                    .data(item)
            }

        this.add(item)
    }

}
