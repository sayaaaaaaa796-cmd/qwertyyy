package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM download_history WHERE platform = :platform ORDER BY timestamp DESC")
    fun getHistoryByPlatform(platform: String): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM download_history WHERE id = :id")
    suspend fun getById(id: Long): DownloadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadEntity): Long

    @Query("DELETE FROM download_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM download_history")
    suspend fun clearAllHistory()
}
