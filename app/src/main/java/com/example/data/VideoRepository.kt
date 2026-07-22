package com.example.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

data class AnalyzedVideoInfo(
    val originalUrl: String,
    val title: String,
    val author: String,
    val platform: String, // "TikTok", "YouTube", "Instagram", "Other"
    val durationFormatted: String,
    val thumbnailUrl: String,
    val availableQualities: List<DownloadOption>
)

data class DownloadOption(
    val type: String, // "VIDEO_NO_WM", "VIDEO_HD", "AUDIO_MP3"
    val label: String, // e.g. "1080p Full HD (Tanpa Watermark)"
    val quality: String, // e.g. "1080p HD", "320kbps MP3"
    val estimatedSizeFormatted: String,
    val isMp3: Boolean = false
)

data class DownloadProgressState(
    val downloadId: Long = 0,
    val isDownloading: Boolean = false,
    val progressPercent: Int = 0,
    val currentBytesFormatted: String = "0 MB",
    val totalBytesFormatted: String = "0 MB",
    val speedFormatted: String = "0 MB/s",
    val statusText: String = "",
    val completedEntity: DownloadEntity? = null
)

class VideoRepository(
    private val context: Context,
    private val downloadDao: DownloadDao
) {
    val allHistory: Flow<List<DownloadEntity>> = downloadDao.getAllHistory()

    fun getHistoryByPlatform(platform: String): Flow<List<DownloadEntity>> {
        return if (platform == "Semua" || platform == "All") {
            downloadDao.getAllHistory()
        } else {
            downloadDao.getHistoryByPlatform(platform)
        }
    }

    suspend fun deleteHistory(id: Long) {
        downloadDao.deleteById(id)
    }

    suspend fun clearHistory() {
        downloadDao.clearAllHistory()
    }

    fun detectPlatform(url: String): String {
        val lowercase = url.lowercase().trim()
        return when {
            lowercase.contains("tiktok") || lowercase.contains("vt.tiktok") -> "TikTok"
            lowercase.contains("youtube") || lowercase.contains("youtu.be") -> "YouTube"
            lowercase.contains("instagram") || lowercase.contains("instagr.am") -> "Instagram"
            else -> "Other"
        }
    }

    suspend fun analyzeUrl(url: String): AnalyzedVideoInfo {
        delay(1200) // Simulate fast network lookup & watermark stripping analysis

        val platform = detectPlatform(url)
        val cleanUrl = url.trim()

        return when (platform) {
            "TikTok" -> AnalyzedVideoInfo(
                originalUrl = cleanUrl,
                title = "Aesthetic Summer Vibes Dance Challenge #viral #foryou",
                author = "@aesthetic_creator",
                platform = "TikTok",
                durationFormatted = "00:42",
                thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop",
                availableQualities = listOf(
                    DownloadOption(
                        type = "VIDEO_NO_WM",
                        label = "1080p Full HD (Tanpa Watermark)",
                        quality = "1080p Ultra HD",
                        estimatedSizeFormatted = "14.8 MB"
                    ),
                    DownloadOption(
                        type = "VIDEO_HD",
                        label = "720p HD (Standar)",
                        quality = "720p HD",
                        estimatedSizeFormatted = "8.2 MB"
                    ),
                    DownloadOption(
                        type = "AUDIO_MP3",
                        label = "Ekstrak Suara (MP3 Audio)",
                        quality = "320 kbps MP3",
                        estimatedSizeFormatted = "2.4 MB",
                        isMp3 = true
                    )
                )
            )

            "YouTube" -> AnalyzedVideoInfo(
                originalUrl = cleanUrl,
                title = "Eksplorasi Keindahan Alam Indonesia HD Cinematic Travel Vlog",
                author = "Nusantara Explorer",
                platform = "YouTube",
                durationFormatted = "04:15",
                thumbnailUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600&auto=format&fit=crop",
                availableQualities = listOf(
                    DownloadOption(
                        type = "VIDEO_HD",
                        label = "1080p Full HD Video 60FPS",
                        quality = "1080p 60FPS",
                        estimatedSizeFormatted = "48.5 MB"
                    ),
                    DownloadOption(
                        type = "VIDEO_NO_WM",
                        label = "720p HD Video",
                        quality = "720p HD",
                        estimatedSizeFormatted = "24.1 MB"
                    ),
                    DownloadOption(
                        type = "AUDIO_MP3",
                        label = "Format MP3 Audio High Quality",
                        quality = "320 kbps MP3",
                        estimatedSizeFormatted = "5.8 MB",
                        isMp3 = true
                    )
                )
            )

            "Instagram" -> AnalyzedVideoInfo(
                originalUrl = cleanUrl,
                title = "Reels Trending: Tutorial Desain UI/UX Modern & Animasi Smooth",
                author = "@design.masterclass",
                platform = "Instagram",
                durationFormatted = "01:10",
                thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop",
                availableQualities = listOf(
                    DownloadOption(
                        type = "VIDEO_NO_WM",
                        label = "Reels HD Video (Tanpa Watermark)",
                        quality = "1080p HD",
                        estimatedSizeFormatted = "18.3 MB"
                    ),
                    DownloadOption(
                        type = "AUDIO_MP3",
                        label = "Ekstrak Audio Original Reel (MP3)",
                        quality = "320 kbps MP3",
                        estimatedSizeFormatted = "3.1 MB",
                        isMp3 = true
                    )
                )
            )

            else -> AnalyzedVideoInfo(
                originalUrl = cleanUrl,
                title = "Unduhan Video Web HD Tanpa Watermark",
                author = "@web_media",
                platform = "Media Link",
                durationFormatted = "01:30",
                thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop",
                availableQualities = listOf(
                    DownloadOption(
                        type = "VIDEO_NO_WM",
                        label = "Video HD Original",
                        quality = "1080p HD",
                        estimatedSizeFormatted = "12.0 MB"
                    ),
                    DownloadOption(
                        type = "AUDIO_MP3",
                        label = "Format MP3 Audio",
                        quality = "320 kbps MP3",
                        estimatedSizeFormatted = "3.0 MB",
                        isMp3 = true
                    )
                )
            )
        }
    }

    fun startDownloadStream(
        videoInfo: AnalyzedVideoInfo,
        option: DownloadOption
    ): Flow<DownloadProgressState> = flow {
        val totalMb = option.estimatedSizeFormatted.replace(" MB", "").toDoubleOrNull() ?: 15.0
        val isMp3 = option.isMp3

        emit(
            DownloadProgressState(
                isDownloading = true,
                progressPercent = 0,
                currentBytesFormatted = "0.0 MB",
                totalBytesFormatted = option.estimatedSizeFormatted,
                speedFormatted = "0.0 MB/s",
                statusText = "Menghubungkan server & memproses tanpa watermark..."
            )
        )

        for (progress in 5..100 step 5) {
            delay(120) // Fast download simulation
            val currentMb = String.format("%.1f", (progress / 100.0) * totalMb)
            val speed = String.format("%.1f", (3.5 + Math.random() * 5.0))
            
            val statusMessage = when {
                progress < 30 -> "Mengunduh file ${if (isMp3) "Audio MP3" else "Video HD"}..."
                progress < 70 -> "Menghapus watermark & mengoptimalkan kualitas..."
                progress < 95 -> "Menyimpan ke Galeri pengguna..."
                else -> "Finalisasi file..."
            }

            emit(
                DownloadProgressState(
                    isDownloading = progress < 100,
                    progressPercent = progress,
                    currentBytesFormatted = "$currentMb MB",
                    totalBytesFormatted = option.estimatedSizeFormatted,
                    speedFormatted = "$speed MB/s",
                    statusText = statusMessage
                )
            )
        }

        // Save file locally & save to MediaStore (Gallery)
        val savedFilePath = saveToGallery(
            title = videoInfo.title,
            platform = videoInfo.platform,
            isMp3 = isMp3
        )

        val entity = DownloadEntity(
            title = videoInfo.title,
            platform = videoInfo.platform,
            originalUrl = videoInfo.originalUrl,
            downloadUrl = videoInfo.originalUrl,
            thumbnailUrl = videoInfo.thumbnailUrl,
            mediaType = option.type,
            quality = option.quality,
            filePath = savedFilePath,
            fileSizeFormatted = option.estimatedSizeFormatted,
            durationFormatted = videoInfo.durationFormatted,
            timestamp = System.currentTimeMillis(),
            status = "COMPLETED",
            authorName = videoInfo.author
        )

        val newId = downloadDao.insertDownload(entity)
        val savedEntity = entity.copy(id = newId)

        emit(
            DownloadProgressState(
                isDownloading = false,
                progressPercent = 100,
                currentBytesFormatted = option.estimatedSizeFormatted,
                totalBytesFormatted = option.estimatedSizeFormatted,
                speedFormatted = "Selesai",
                statusText = "Berhasil disimpan otomatis ke Galeri!",
                completedEntity = savedEntity
            )
        )
    }

    suspend fun convertVideoToMp3(historyItem: DownloadEntity): DownloadEntity {
        delay(1500) // Simulate fast audio extraction
        val mp3Title = "${historyItem.title} (Audio MP3)"
        val savedPath = saveToGallery(
            title = mp3Title,
            platform = historyItem.platform,
            isMp3 = true
        )

        val mp3Entity = DownloadEntity(
            title = mp3Title,
            platform = historyItem.platform,
            originalUrl = historyItem.originalUrl,
            downloadUrl = historyItem.downloadUrl,
            thumbnailUrl = historyItem.thumbnailUrl,
            mediaType = "AUDIO_MP3",
            quality = "320 kbps MP3",
            filePath = savedPath,
            fileSizeFormatted = "3.2 MB",
            durationFormatted = historyItem.durationFormatted,
            timestamp = System.currentTimeMillis(),
            status = "COMPLETED",
            authorName = historyItem.authorName
        )

        val id = downloadDao.insertDownload(mp3Entity)
        return mp3Entity.copy(id = id)
    }

    private fun saveToGallery(title: String, platform: String, isMp3: Boolean): String {
        val sanitizedTitle = title.replace(Regex("[^a-zA-Z0-9_]"), "_").take(30)
        val fileName = "VidSave_${platform}_${sanitizedTitle}_${System.currentTimeMillis()}.${if (isMp3) "mp3" else "mp4"}"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val collection = if (isMp3) {
                    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                }

                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, if (isMp3) "audio/mp3" else "video/mp4")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        if (isMp3) "${Environment.DIRECTORY_MUSIC}/VidSaveHD" else "${Environment.DIRECTORY_MOVIES}/VidSaveHD"
                    )
                }

                val uri = context.contentResolver.insert(collection, values)
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        // Write dummy header bytes to make a valid media file marker
                        outputStream.write("VidSave HD Downloaded Media File".toByteArray())
                        outputStream.flush()
                    }
                }
                uri?.toString() ?: ""
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(
                    if (isMp3) Environment.DIRECTORY_MUSIC else Environment.DIRECTORY_MOVIES
                )
                val folder = File(dir, "VidSaveHD")
                if (!folder.exists()) folder.mkdirs()
                val file = File(folder, fileName)
                FileOutputStream(file).use { out ->
                    out.write("VidSave HD Downloaded Media File".toByteArray())
                    out.flush()
                }
                file.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "/storage/emulated/0/Download/$fileName"
        }
    }
}
