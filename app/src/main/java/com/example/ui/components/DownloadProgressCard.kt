package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DownloadEntity
import com.example.data.DownloadProgressState
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen

@Composable
fun DownloadProgressCard(
    state: DownloadProgressState,
    onOpenCompleted: (DownloadEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!state.isDownloading && state.completedEntity == null && state.progressPercent == 0) {
        return
    }

    AnimatedVisibility(
        visible = state.isDownloading || state.completedEntity != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(NeonCyan, NeonPurple)),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = DarkSurface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (state.progressPercent == 100) SuccessGreen.copy(alpha = 0.2f)
                                    else ElectricIndigo.copy(alpha = 0.2f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (state.progressPercent == 100) Icons.Default.CheckCircle else Icons.Default.Download,
                                contentDescription = null,
                                tint = if (state.progressPercent == 100) SuccessGreen else NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (state.progressPercent == 100) "Unduhan Selesai!" else "Mengunduh Video HD...",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                text = state.statusText,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = "${state.progressPercent}%",
                        fontWeight = FontWeight.ExtraBold,
                        color = if (state.progressPercent == 100) SuccessGreen else NeonCyan,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { state.progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = if (state.progressPercent == 100) SuccessGreen else NeonCyan,
                    trackColor = DarkSurfaceBorder
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${state.currentBytesFormatted} / ${state.totalBytesFormatted}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.speedFormatted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonPurple
                    )
                }

                if (state.completedEntity != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onOpenCompleted(state.completedEntity) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessGreen
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Buka & Putar Media",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
