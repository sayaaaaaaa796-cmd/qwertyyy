package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.DownloadEntity
import com.example.ui.components.DownloadProgressCard
import com.example.ui.components.PlatformBadge
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.InstagramPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.YouTubeRed
import com.example.viewmodel.AnalysisState
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onOpenMedia: (DownloadEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val urlInput by viewModel.urlInput.collectAsStateWithLifecycle()
    val detectedPlatform by viewModel.detectedPlatform.collectAsStateWithLifecycle()
    val analysisState by viewModel.analysisState.collectAsStateWithLifecycle()
    val activeDownload by viewModel.activeDownload.collectAsStateWithLifecycle()
    val recentHistory by viewModel.historyList.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Hero Banner Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner_1784717664575),
                    contentDescription = "Hero Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(listOf(NeonCyan, NeonPurple)),
                                    CircleShape
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "PRO HD DOWNLOADER",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Unduh Video TikTok, YouTube, IG Tanpa Watermark",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Supported Platforms Pill Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Platform Didukung:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (detectedPlatform != "Other") {
                PlatformBadge(platform = detectedPlatform)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("TikTok", "YouTube", "Instagram").forEach { platform ->
                val isDetected = detectedPlatform.equals(platform, ignoreCase = true)
                val color = when (platform) {
                    "TikTok" -> TikTokRed
                    "YouTube" -> YouTubeRed
                    "Instagram" -> InstagramPink
                    else -> ElectricIndigo
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDetected) color.copy(alpha = 0.25f) else DarkSurface)
                        .border(
                            width = 1.dp,
                            color = if (isDetected) color else DarkSurfaceBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.setSampleUrl(platform) }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlatformBadge(platform = platform)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Link Input Box Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tempel Tautan Video Di Sini:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { viewModel.onUrlInputChanged(it) },
                    placeholder = {
                        Text(
                            text = "https://vt.tiktok.com/... atau youtube.com / instagram...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = NeonCyan
                        )
                    },
                    trailingIcon = {
                        if (urlInput.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onUrlInputChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Hapus",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            IconButton(onClick = { viewModel.pasteFromClipboard() }) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Tempel",
                                    tint = NeonCyan
                                )
                            }
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons: Paste & Analyze
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.pasteFromClipboard() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tempel Link",
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.analyzeUrl() },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
                        enabled = analysisState !is AnalysisState.Analyzing
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(listOf(NeonCyan, ElectricIndigo)),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (analysisState is AnalysisState.Analyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Analisis Video",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Download Progress Card
        DownloadProgressCard(
            state = activeDownload,
            onOpenCompleted = onOpenMedia
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Feature Highlights Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface, shape = RoundedCornerShape(14.dp))
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FeaturePill(icon = Icons.Default.HighQuality, text = "Kualitas HD 1080p", color = NeonCyan)
            FeaturePill(icon = Icons.Default.Security, text = "Tanpa Watermark", color = SuccessGreen)
            FeaturePill(icon = Icons.Default.MusicNote, text = "Ekstrak MP3", color = NeonPurple)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Downloaded Items Header
        if (recentHistory.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unduhan Terbaru:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Lihat Semua (${recentHistory.size})",
                    fontSize = 12.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.selectedTab.value = 1 }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(recentHistory.take(5)) { item ->
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .clickable { onOpenMedia(item) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(DarkSurfaceVariant)
                            ) {
                                PlatformBadge(
                                    platform = item.platform,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(6.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(36.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = NeonCyan
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Text(
                                    text = item.quality,
                                    fontSize = 10.sp,
                                    color = NeonPurple,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturePill(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}
