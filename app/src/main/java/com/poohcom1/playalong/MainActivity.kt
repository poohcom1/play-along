package com.poohcom1.playalong

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poohcom1.playalong.ui.scenes.SongController
import com.poohcom1.playalong.ui.theme.PlayAlongTheme
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: wtf is this for?
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            YoutubeDL.getInstance().init(applicationContext)
            YoutubeDL.getInstance()
                .updateYoutubeDL(applicationContext, YoutubeDL.UpdateChannel.STABLE)
        } catch (e: YoutubeDLException) {
            Log.e("YoutubeDL", e.toString())
        }

        setContent {
            PlayAlongTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainContainer()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(modifier: Modifier = Modifier) {
    var loading by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var videoInfo by remember { mutableStateOf<VideoInfo?>(null) }

    val composableScope = rememberCoroutineScope()

    Column {
        if (loading) {
            Text(text = "Loading...")
        } else videoInfo?.let { SongController(info = it, modifier = modifier) }

        Row() {
            Button(onClick = {
                showPopup = true
            }) {
                Text(text = "Select song")
            }
        }
    }
    if (showPopup) {
        var youtubeUrl by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showPopup = false }) {
            Card() {
                Column(Modifier.padding(16.dp)) {
                    TextField(
                        value = youtubeUrl,
                        onValueChange = { youtubeUrl = it },
                        placeholder = { Text(text = "Enter URL") },
                        singleLine = true,
                    )
                    Button(onClick = {
                        showPopup = false
                        loading = true

                        // Youtube DL Coroutine
                        composableScope.launch(Dispatchers.IO) {
                            videoInfo = try {
                                val getUrlRequest = YoutubeDLRequest(youtubeUrl)
                                getUrlRequest.addOption("-f", "best")

                                YoutubeDL.getInstance().getInfo(getUrlRequest)
                            } catch (e: Exception) {
                                null
                            }
                            loading = false
                        }

                    }) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    PlayAlongTheme {
        MainContainer()
    }
}