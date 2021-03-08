package com.igalogs.jinlog.data.log

import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Log
import java.util.*

class LogDataRepository(private var dataSource: LogDataSource) : LogRepository {

    override suspend fun getLogs(limit: Int, startAtDate: Date?): Result<List<Log>> {
        return dataSource.getLogs(limit, startAtDate)
    }

    override suspend fun getLogsByPlaceId(placeId: String): Result<List<Log>> {
        return dataSource.getLogsByPlaceId(placeId)
    }
}