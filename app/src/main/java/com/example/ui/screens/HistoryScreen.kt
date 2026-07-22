package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.data.DownloadEntity
import com.example.ui.components.PlatformBadge
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.MainViewModel

@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    onOpenMedia: (DownloadEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedPlatformFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Search & Clear Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Riwayat Unduhan Media",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (historyList.isNotEmpty()) {
                IconButton(onClick = { showClearDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Bersihkan Riwayat",
                        tint = ErrorRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchQuery.value = it },
            placeholder = { Text("Cari judul video, kreator, atau platform...", fontSize = 13.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NeonCyan)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Platform Filters Row
        val filters = listOf("Semua", "TikTok", "YouTube", "Instagram", "MP3 Audio")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filterName ->
                val isSelected = selectedFilter == filterName
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectedPlatformFilter.value = filterName },
                    label = {
                        Text(
                            text = filterName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricIndigo,
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Empty State or List
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img_empty_history_1784717676564),
                        contentDescription = "Empty History",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Belum Ada Riwayat Unduhan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tautan video TikTok, YouTube, atau Instagram yang kamu unduh akan tersimpan rapi di sini.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        lineHeight = 18.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(historyList, key = { it.id }) { item ->
                    val isAudio = item.mediaType == "AUDIO_MP3"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
                            .clickable { onOpenMedia(item) },
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Thumbnail Box
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(DarkSurfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = item.thumbnailUrl,
                                        contentDescription = item.title,
                                        modifier = Modifier.matchParentSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isAudio) Icons.Default.MusicNote else Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = if (isAudio) NeonPurple else NeonCyan,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Details Column
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        PlatformBadge(platform = item.platform)

                                        IconButton(
                                            onClick = { viewModel.deleteHistoryItem(item.id) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Hapus",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = item.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${item.quality} • ${item.fileSizeFormatted}",
                                            fontSize = 11.sp,
                                            color = if (isAudio) NeonPurple else NeonCyan,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Text(
                                            text = "Tersimpan",
                                            fontSize = 10.sp,
                                            color = SuccessGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Bottom Item Actions Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onOpenMedia(item) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Putar", fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                                }

                                if (!isAudio) {
                                    Button(
                                        onClick = { viewModel.convertHistoryToMp3(item) },
                                        modifier = Modifier
                                            .weight(1.3f)
                                            .height(36.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo.copy(alpha = 0.3f)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Transform, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Konversi MP3", fontSize = 11.sp, color = NeonPurple, fontWeight = FontWeight.Bold)
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        val shareIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, "Unduhan VidSave HD:\n${item.originalUrl}")
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Tautan"))
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(DarkSurfaceVariant, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "Bagikan", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Clear Confirmation Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Hapus Semua Riwayat?", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah kamu yakin ingin membersihkan seluruh daftar riwayat unduhan?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Ya, Hapus Semua", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Batal")
                }
            },
            containerColor = DarkSurface
        )
    }
}
