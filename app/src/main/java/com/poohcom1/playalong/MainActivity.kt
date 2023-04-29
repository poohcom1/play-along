package com.poohcom1.playalong

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poohcom1.playalong.ui.components.VideoUrlInput
import com.poohcom1.playalong.ui.scenes.ControlPanel
import com.poohcom1.playalong.ui.scenes.PlayerController
import com.poohcom1.playalong.ui.theme.PlayAlongTheme
import com.poohcom1.playalong.utils.TempoTapCalculator
import com.poohcom1.playalong.utils.getVideoInfo
import com.poohcom1.playalong.viewmodels.PopupType
import com.poohcom1.playalong.viewmodels.UiState
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
      YoutubeDL.getInstance().updateYoutubeDL(applicationContext, YoutubeDL.UpdateChannel.STABLE)
    } catch (e: YoutubeDLException) {
      Log.e("YoutubeDL", e.toString())
    }

    setContent {
      PlayAlongTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MainContainer()
        }
      }
    }
  }
}

@Composable
fun MainContainer() {
  val context = LocalContext.current
  val composableScope = rememberCoroutineScope()

  // Rendering states
  var loading by remember { mutableStateOf(false) }
  var videoInfo by remember { mutableStateOf<VideoInfo?>(null) }

  // States
  val (uiState, setUiState) = remember { mutableStateOf(UiState()) }

  // Dependencies
  val tempoTapCalculator = remember { TempoTapCalculator(2000, true) }
  val mediaPlayer = remember { MediaPlayer.create(context, R.raw.metronome_click) }

  val fetchVideoInfo: (String) -> Unit = {
    loading = true

    // Youtube DL Coroutine
    composableScope.launch(Dispatchers.IO) {
      Looper.prepare()

      getVideoInfo(it)
          .onSuccess { videoInfo = it }
          .onFailure { Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show() }

      loading = false
    }
  }

  LaunchedEffect(uiState) {
    if (uiState.player != null) {
      tempoTapCalculator.player = uiState.player
    }
  }

  Column(Modifier.padding(8.dp).fillMaxHeight()) {
    ControlPanel(uiState = uiState, onRootStateChanged = setUiState)

    videoInfo?.let { info ->
      PlayerController(info = info, uiState, onRootStateChanged = setUiState)
    }
        ?: run {
          Spacer(Modifier.height(8.dp))
          Surface(
              tonalElevation = 0.dp,
              shape = MaterialTheme.shapes.medium,
              modifier = Modifier.fillMaxSize()) {
                if (loading) {
                  Text(text = "Loading...")
                } else {
                  Column(
                      verticalArrangement = Arrangement.Center,
                      horizontalAlignment = Alignment.CenterHorizontally,
                      modifier = Modifier.weight(1f).fillMaxSize()) {
                        VideoUrlInput(fetchVideoInfo)
                      }
                }
              }
        }
  }

  when (uiState.popup) {
    PopupType.VIDEO_URL -> {
      Dialog(onDismissRequest = { setUiState(uiState.copy(popup = PopupType.NONE)) }) {
        VideoUrlInput(onSubmit = fetchVideoInfo)
      }
    }
    PopupType.NONE -> Unit
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
  PlayAlongTheme { MainContainer() }
}
