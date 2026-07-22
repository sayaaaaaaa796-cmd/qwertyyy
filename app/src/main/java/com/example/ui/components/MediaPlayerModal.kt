package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.DownloadEntity
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPlayerModal(
    item: DownloadEntity,
    onConvertToMp3: (DownloadEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isPlaying by remember { mutableStateOf(true) }
    var sliderPosition by remember { mutableFloatStateOf(0.35f) }
    val isAudio = item.mediaType == "AUDIO_MP3"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkSurface,
        scrimColor = Color.Black.copy(alpha = 0.75f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlatformBadge(platform = item.platform)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAudio) "Pemutar Audio MP3" else "Pemutar Video HD",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Player Canvas Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = item.title,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for audio or video controls visibility
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = if (isAudio) 0.65f else 0.35f))
                )

                if (isAudio) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(NeonPurple.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Audio MP3 HD 320kbps",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Play / Pause Overlay Button
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = NeonCyan,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Media Title & Specs
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kreator: ${item.authorName}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${item.quality} • ${item.fileSizeFormatted}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Playback Seeking Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = DarkSurfaceBorder
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "00:15", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = item.durationFormatted, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!isAudio) {
                    Button(
                        onClick = { onConvertToMp3(item) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Transform, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Konversi MP3", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Lihat video/audio ini yang saya unduh tanpa watermark pakai VidSave HD:\n${item.originalUrl}")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Media"))
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Bagikan Tautan", fontSize = 13.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
