package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_history")
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val platform: String, // "TikTok", "YouTube", "Instagram", "Other"
    val originalUrl: String,
    val downloadUrl: String = "",
    val thumbnailUrl: String = "",
    val mediaType: String, // "VIDEO_HD", "VIDEO_NO_WM", "AUDIO_MP3"
    val quality: String = "1080p Full HD",
    val filePath: String = "",
    val fileSizeFormatted: String = "12.5 MB",
    val durationFormatted: String = "00:45",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "COMPLETED", // "COMPLETED", "DOWNLOADING", "FAILED"
    val authorName: String = "@creator"
)
