package com.example.samplevideosapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                VideoPlayerScreen()
            }

        }
    }
}

@Composable
fun VideoPlayerScreen() {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setLoadControl(DefaultLoadControl())
            .build()
    }

    val videoUrlHD = "https://upload.wikimedia.org/wikipedia/commons/c/c0/Big_Buck_Bunny_4K.webm"
    val videoUrlLQ = "https://upload.wikimedia.org/wikipedia/commons/3/37/Big_Buck_Bunny_with_VTT_acid_test.webm"
    var isPlayingHD by remember { mutableStateOf(true) }
    val videoUrl = if (isPlayingHD) videoUrlHD else videoUrlLQ

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    val dataSourceFactory = DefaultDataSourceFactory(context)
    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))

    exoPlayer.setMediaSource(mediaSource)
    exoPlayer.prepare()
    exoPlayer.playWhenReady = true

    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            onClick = {
                isPlayingHD = !isPlayingHD
                val newVideoUrl = if (isPlayingHD) videoUrlHD else videoUrlLQ
                val newMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(newVideoUrl)))
                exoPlayer.setMediaSource(newMediaSource)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
        ) {
            Text(text = "Toggle Video Quality")
        }
    }
}