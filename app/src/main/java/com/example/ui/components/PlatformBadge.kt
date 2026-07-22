package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InstagramPink
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.YouTubeRed

@Composable
fun PlatformBadge(
    platform: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (platform.lowercase()) {
        "tiktok" -> Triple(TikTokRed, Color.White, Icons.Default.MusicNote)
        "youtube" -> Triple(YouTubeRed, Color.White, Icons.Default.PlayCircle)
        "instagram" -> Triple(InstagramPink, Color.White, Icons.Default.Videocam)
        else -> Triple(Color(0xFF334155), Color.White, Icons.Default.PlayCircle)
    }

    Row(
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = platform,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = platform,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
