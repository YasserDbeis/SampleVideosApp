package com.example.samplevideosapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.samplevideosapp.ui.theme.Shapes
import com.google.android.exoplayer2.util.Util

class MainActivity : ComponentActivity() {

    private val exoPlayer: SimpleExoPlayer by lazy { SimpleExoPlayer.Builder(this).build() }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column {
                    VideoPlayerScreen(exoPlayer)
                    ShoppingAppHomeScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    @OptIn(ExperimentalUnitApi::class)
    @Composable
    fun VideoPlayerScreen(exoPlayer: SimpleExoPlayer) {
        val context = LocalContext.current

        // Set up PlayerView with ExoPlayer
        val playerView = androidx.compose.ui.viewinterop.AndroidView({ ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        })

        // Prepare and play video
        val videoUrlHD =
            "https://upload.wikimedia.org/wikipedia/commons/c/c0/Big_Buck_Bunny_4K.webm"
        val videoUrlLQ =
            "https://upload.wikimedia.org/wikipedia/commons/3/37/Big_Buck_Bunny_with_VTT_acid_test.webm"
        val videoUrl = videoUrlHD
        val isVideoHD = videoUrl == videoUrlHD
        prepareAndPlayVideo(context, exoPlayer, videoUrl)

        Box(
            contentAlignment = Alignment.Center
        ) {
            playerView

            Text(
                text = "Playing in ${if (isVideoHD) "HD" else "Low Quality"}",
                color = Color.Red,
                fontSize = TextUnit(24f, TextUnitType.Sp)
            )
        }
    }

    private fun prepareAndPlayVideo(
        context: android.content.Context,
        exoPlayer: SimpleExoPlayer,
        videoUrl: String
    ) {
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "com.example.samplevideosapp")
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl))

        exoPlayer.prepare(mediaSource)
        exoPlayer.play()
    }

    @Composable
    fun ShoppingAppHomeScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            TopAppBar(
                title = { Text(text = "Bookstore App") },
                actions = {
                    // Shopping Cart Icon
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                    )
                },
                elevation = 8.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Search for books") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Genre Selections
            Text(
                text = "Genres",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                content = {
                    items(5) {
                        GenreSelectionItem(index = it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Featured Books
            Text(
                text = "Featured Books",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn {
                items(6) { index ->
                    FeaturedBookItem(index = index)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    @Composable
    fun GenreSelectionItem(index: Int) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .width(100.dp)
                .clip(Shapes.medium)
                .background(MaterialTheme.colors.surface)
                .clickable { /* Handle genre selection click */ }
        ) {
            Text(
                text = "Genre $index",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(88.dp)
                    .clip(Shapes.medium)
                    .background(MaterialTheme.colors.onSurface),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray) // Change to your desired color
            )
        }
    }

    @Composable
    fun FeaturedBookItem(index: Int) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(Shapes.medium)
                .background(MaterialTheme.colors.surface)
                .clickable { /* Handle book item click */ }
                .padding(16.dp)
        ) {
            // Book Cover Image
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(88.dp)
                    .background(MaterialTheme.colors.onSurface),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray) // Change to your desired color
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Book Details
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Book $index", style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$12.99", style = MaterialTheme.typography.body1)
            }
        }
    }
}