package com.igalogs.jinlog.data.log

import com.google.firebase.firestore.FirebaseFirestore
import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class FirestoreLogDataSource(
    private var firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LogDataSource {

    val logCollectionGroup = firestore.collectionGroup("logs")

    override suspend fun getLogs(limit: Int, startAtDate: Date?): Result<List<Log>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val query = logCollectionGroup
                    .limit(limit.toLong())

                if (startAtDate != null) {
                    query.startAfter("createdDate", startAtDate)
                }

                val data = query.get().await().toObjects(Log::class.java)

                Result.Success(data ?: listOf())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getLogsByPlaceId(placeId: String): Result<List<Log>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val query = logCollectionGroup.whereEqualTo("placeId", placeId)
                val data = query.get().await().toObjects(Log::class.java)
                Result.Success(data ?: listOf())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

}
