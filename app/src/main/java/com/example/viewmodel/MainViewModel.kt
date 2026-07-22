package com.example.viewmodel

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AnalyzedVideoInfo
import com.example.data.DownloadEntity
import com.example.data.DownloadOption
import com.example.data.DownloadProgressState
import com.example.data.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Analyzing : AnalysisState()
    data class Success(val info: AnalyzedVideoInfo) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VideoRepository
    
    val urlInput = MutableStateFlow("")
    val detectedPlatform = MutableStateFlow("Other")
    
    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    private val _activeDownload = MutableStateFlow(DownloadProgressState())
    val activeDownload: StateFlow<DownloadProgressState> = _activeDownload.asStateFlow()

    val selectedPlatformFilter = MutableStateFlow("Semua")
    val searchQuery = MutableStateFlow("")
    val selectedTab = MutableStateFlow(0)

    val activeMediaPreview = MutableStateFlow<DownloadEntity?>(null)
    val userNotification = MutableStateFlow<String?>(null)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = VideoRepository(application, db.downloadDao())
    }

    // Filtered history list from Room DB
    val historyList: StateFlow<List<DownloadEntity>> = combine(
        repository.allHistory,
        selectedPlatformFilter,
        searchQuery
    ) { history, filter, query ->
        history.filter { item ->
            val matchesFilter = when (filter) {
                "Semua" -> true
                "MP3 Audio" -> item.mediaType == "AUDIO_MP3"
                else -> item.platform.equals(filter, ignoreCase = true)
            }

            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                item.title.contains(query, ignoreCase = true) ||
                item.authorName.contains(query, ignoreCase = true) ||
                item.platform.contains(query, ignoreCase = true)
            }

            matchesFilter && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onUrlInputChanged(newUrl: String) {
        urlInput.value = newUrl
        detectedPlatform.value = repository.detectPlatform(newUrl)
        if (newUrl.isBlank()) {
            _analysisState.value = AnalysisState.Idle
        }
    }

    fun pasteFromClipboard() {
        try {
            val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboard.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val pastedText = clipData.getItemAt(0).text?.toString() ?: ""
                if (pastedText.isNotBlank()) {
                    onUrlInputChanged(pastedText)
                    userNotification.value = "Tautan berhasil ditempel dari Clipboard!"
                    analyzeUrl()
                } else {
                    userNotification.value = "Clipboard kosong."
                }
            } else {
                userNotification.value = "Tidak ada teks di Clipboard."
            }
        } catch (e: Exception) {
            userNotification.value = "Gagal membaca Clipboard: ${e.message}"
        }
    }

    fun setSampleUrl(platform: String) {
        val sampleUrl = when (platform) {
            "TikTok" -> "https://vt.tiktok.com/ZSj8x9L2M/"
            "YouTube" -> "https://youtube.com/watch?v=dQw4w9WgXcQ"
            "Instagram" -> "https://www.instagram.com/reel/C9x81YpMBx2/"
            else -> "https://vt.tiktok.com/ZSj8x9L2M/"
        }
        onUrlInputChanged(sampleUrl)
        analyzeUrl()
    }

    fun analyzeUrl() {
        val currentUrl = urlInput.value.trim()
        if (currentUrl.isEmpty()) {
            userNotification.value = "Masukkan tautan video TikTok, YouTube, atau Instagram dulu."
            return
        }

        viewModelScope.launch {
            _analysisState.value = AnalysisState.Analyzing
            try {
                val info = repository.analyzeUrl(currentUrl)
                _analysisState.value = AnalysisState.Success(info)
            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error("Gagal menganalisis tautan. Pastikan tautan valid.")
            }
        }
    }

    fun startDownload(videoInfo: AnalyzedVideoInfo, option: DownloadOption) {
        viewModelScope.launch {
            _analysisState.value = AnalysisState.Idle // close modal
            repository.startDownloadStream(videoInfo, option).collect { state ->
                _activeDownload.value = state
                if (state.completedEntity != null) {
                    userNotification.value = "Selesai! Video tersimpan otomatis ke Galeri."
                }
            }
        }
    }

    fun convertHistoryToMp3(historyItem: DownloadEntity) {
        viewModelScope.launch {
            userNotification.value = "Mengkonversi video ke MP3 Audio..."
            try {
                val mp3Item = repository.convertVideoToMp3(historyItem)
                userNotification.value = "Berhasil dikonversi ke MP3! Tersimpan di Galeri/Musik."
            } catch (e: Exception) {
                userNotification.value = "Gagal konversi MP3: ${e.message}"
            }
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteHistory(id)
            userNotification.value = "Item riwayat dihapus."
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            userNotification.value = "Semua riwayat unduhan dibersihkan."
        }
    }

    fun openMediaPreview(item: DownloadEntity) {
        activeMediaPreview.value = item
    }

    fun closeMediaPreview() {
        activeMediaPreview.value = null
    }

    fun dismissNotification() {
        userNotification.value = null
    }
}
