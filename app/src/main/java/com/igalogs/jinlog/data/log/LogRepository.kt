package com.igalogs.jinlog.data.log

import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Log
import java.util.*

interface LogRepository {

    suspend fun getLogs(limit: Int, startAtDate: Date?) : Result<List<Log>>

}