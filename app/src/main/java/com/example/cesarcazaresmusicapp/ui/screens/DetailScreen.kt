package com.example.cesarcazaresmusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cesarcazaresmusicapp.data.model.AlbumDetail
import com.example.cesarcazaresmusicapp.ui.viewmodel.MusicViewModel
import com.example.cesarcazaresmusicapp.ui.viewmodel.UiState

@Composable
fun DetailScreen(viewModel: MusicViewModel, albumId: String, onBackClick: () -> Unit) {
    LaunchedEffect(albumId) {
        viewModel.fetchAlbumDetail(albumId)
    }

    val detailState by viewModel.albumDetailState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE8FF))
    ) {
        when (val state = detailState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Error -> {
                Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Success -> {
                val album = state.data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        DetailHeader(album = album, onBackClick = onBackClick)
                    }
                    item {
                        AboutCard(description = album.description ?: "No description available.")
                    }
                    item {
                        ArtistChip(artist = album.artist)
                    }
                    items(10) { index ->
                        TrackItem(album = album, trackNumber = index + 1)
                    }
                }
                
                // Floating MiniPlayer at the bottom
                Box(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                    MiniPlayer(
                        title = album.title,
                        artist = album.artist,
                        cover = album.cover
                    )
                }
            }
        }
    }
}

@Composable
fun DetailHeader(album: AlbumDetail, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        AsyncImage(
            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(album.cover)
                .crossfade(true)
                .setHeader("User-Agent", "Mozilla/5.0")
                .build(),
            contentDescription = "Album cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Purple Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF3B2787).copy(alpha = 0.95f)),
                        startY = 100f
                    )
                )
        )
        // Top Bar Icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
            }
        }
        // Album Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(text = album.title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = album.artist, color = Color.LightGray, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF7C3AED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Shuffle", tint = Color(0xFF2D1B69), modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun AboutCard(description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "About this album", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2D1B69))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun ArtistChip(artist: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row {
            Text(text = "Artist: ", fontWeight = FontWeight.Bold, color = Color(0xFF2D1B69))
            Text(text = artist, color = Color.Gray)
        }
    }
}

@Composable
fun TrackItem(album: AlbumDetail, trackNumber: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(album.cover)
                    .crossfade(true)
                    .setHeader("User-Agent", "Mozilla/5.0")
                    .build(),
                contentDescription = "Album cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${album.title} • Track $trackNumber", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text(text = album.artist, color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
        }
    }
}
