package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.GuideSection
import com.example.ui.components.MediaPlayerModal
import com.example.ui.components.VideoAnalysisModal
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.viewmodel.AnalysisState
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val analysisState by viewModel.analysisState.collectAsStateWithLifecycle()
    val activeMediaPreview by viewModel.activeMediaPreview.collectAsStateWithLifecycle()
    val notificationText by viewModel.userNotification.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notificationText) {
        notificationText?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissNotification()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1784717648470),
                            contentDescription = "VidSave Logo",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .border(1.dp, NeonCyan, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "VidSave",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "HD",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = NeonCyan
                                )
                            }
                            Text(
                                text = "Tersimpan Otomatis Ke Galeri",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(
                                Brush.horizontalGradient(listOf(ElectricIndigo, NeonPurple)),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NO WATERMARK",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.selectedTab.value = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Unduh"
                        )
                    },
                    label = { Text("Unduh", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = ElectricIndigo.copy(alpha = 0.4f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.selectedTab.value = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Riwayat"
                        )
                    },
                    label = { Text("Riwayat", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = ElectricIndigo.copy(alpha = 0.4f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.selectedTab.value = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Panduan"
                        )
                    },
                    label = { Text("Panduan", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = ElectricIndigo.copy(alpha = 0.4f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onOpenMedia = { viewModel.openMediaPreview(it) }
                )
                1 -> HistoryScreen(
                    viewModel = viewModel,
                    onOpenMedia = { viewModel.openMediaPreview(it) }
                )
                2 -> GuideSection()
            }
        }
    }

    // Modal Sheet for Video Analysis Options
    if (analysisState is AnalysisState.Success) {
        val info = (analysisState as AnalysisState.Success).info
        VideoAnalysisModal(
            info = info,
            onStartDownload = { option -> viewModel.startDownload(info, option) },
            onDismiss = { viewModel.onUrlInputChanged("") }
        )
    }

    // Modal Sheet for Media Player Preview
    activeMediaPreview?.let { item ->
        MediaPlayerModal(
            item = item,
            onConvertToMp3 = { viewModel.convertHistoryToMp3(it) },
            onDismiss = { viewModel.closeMediaPreview() }
        )
    }
}
