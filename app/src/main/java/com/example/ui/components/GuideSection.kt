package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.InstagramPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.YouTubeRed

data class PlatformGuide(
    val platform: String,
    val color: Color,
    val steps: List<String>
)

@Composable
fun GuideSection(
    modifier: Modifier = Modifier
) {
    val guides = listOf(
        PlatformGuide(
            platform = "TikTok",
            color = TikTokRed,
            steps = listOf(
                "Buka aplikasi TikTok dan cari video yang ingin kamu unduh.",
                "Tekan tombol 'Bagikan' (Share) di sebelah kanan video.",
                "Pilih opsi 'Salin Tautan' (Copy Link).",
                "Kembali ke aplikasi VidSave HD dan tekan 'Tempel Tautan'."
            )
        ),
        PlatformGuide(
            platform = "YouTube",
            color = YouTubeRed,
            steps = listOf(
                "Buka aplikasi YouTube atau browser dan pilih video / Shorts.",
                "Tekan tombol 'Bagikan' di bawah pemutar video.",
                "Klik 'Salin Tautan' (Copy Link).",
                "Tempelkan tautan di VidSave HD untuk memilih kualitas HD / MP3."
            )
        ),
        PlatformGuide(
            platform = "Instagram",
            color = InstagramPink,
            steps = listOf(
                "Buka Instagram dan pilih Reel, Post, atau IGTV yang kamu suka.",
                "Ketuk ikon pesawat kertas / titik tiga '...' di pojok video.",
                "Pilih 'Salin Tautan' (Copy Link).",
                "Buka VidSave HD dan unduh video tanpa watermark langsung ke Galeri."
            )
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ElectricIndigo.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Panduan Mengunduh Video HD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Ikuti langkah mudah di bawah untuk mengunduh dari TikTok, YouTube, atau Instagram tanpa watermark.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        itemsIndexed(guides) { _, guide ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        PlatformBadge(platform = guide.platform)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Cara Unduh dari ${guide.platform}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    guide.steps.forEachIndexed { index, stepText ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(guide.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = guide.color
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = stepText,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
