package com.poohcom1.playalong

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.poohcom1.playalong.models.ControlPanelSettings
import com.poohcom1.playalong.models.PopupType
import com.poohcom1.playalong.ui.scenes.ControlPanel
import com.poohcom1.playalong.ui.scenes.PlayerController
import com.poohcom1.playalong.ui.scenes.VideoUrlPopup
import com.poohcom1.playalong.ui.theme.PlayAlongTheme
import com.poohcom1.playalong.utils.TempoTapCalculator
import com.poohcom1.playalong.utils.getVideoInfo
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
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
fun MainContainer() {
    val context = LocalContext.current
    val composableScope = rememberCoroutineScope()

    // Rendering states
    var loading by remember { mutableStateOf(false) }
    var videoInfo by remember { mutableStateOf<VideoInfo?>(null) }

    // States
    var controlPanelSettings by remember { mutableStateOf(ControlPanelSettings()) }

    // Dependencies
    val tempoTapCalculator = remember { TempoTapCalculator(2000, true) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.metronome_click) }

    Column {
        ControlPanel(
            setting = controlPanelSettings,
            onSettingChanged = { controlPanelSettings = it })

        if (loading) {
            Text(text = "Loading...")
        } else videoInfo?.let { info ->
            PlayerController(
                info = info,
                controlPanelSettings,
                onSettingsChange = { controlPanelSettings = it })
        }

        Row() {
            Button(onClick = {
                controlPanelSettings = controlPanelSettings.copy(popup = PopupType.VIDEO_URL)
            }) {
                Text(text = "Select song")
            }
        }
    }

    when (controlPanelSettings.popup) {
        PopupType.VIDEO_URL -> {
            VideoUrlPopup(onDismiss = {
                controlPanelSettings = controlPanelSettings.copy(popup = PopupType.NONE)
            }, onUrlSubmit = {
                loading = true

                // Youtube DL Coroutine
                composableScope.launch(Dispatchers.IO) {
                    Looper.prepare()

                    getVideoInfo(it).onSuccess {
                        videoInfo = it
                    }.onFailure {
                        Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
                    }

                    loading = false
                }

            })
        }

        PopupType.NONE -> Unit
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    PlayAlongTheme {
        MainContainer()
    }
}