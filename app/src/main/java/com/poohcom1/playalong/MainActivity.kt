package com.poohcom1.playalong

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.ui.components.Loading
import com.poohcom1.playalong.ui.components.LoopRangeSelector
import com.poohcom1.playalong.ui.components.TempoPicker
import com.poohcom1.playalong.ui.components.VideoPlayer
import com.poohcom1.playalong.ui.components.VideoUrlInput
import com.poohcom1.playalong.ui.scenes.ControlPanel
import com.poohcom1.playalong.ui.theme.PlayAlongTheme
import com.poohcom1.playalong.utils.Metronome
import com.poohcom1.playalong.utils.TempoTapCalculator
import com.poohcom1.playalong.utils.validateYoutubeUrl
import com.poohcom1.playalong.viewmodels.RootState
import com.poohcom1.playalong.viewmodels.UiState
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

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

  // States
  val (uiState, setUiState) = remember { mutableStateOf(UiState()) }
  var rootState by remember { mutableStateOf(RootState()) }

  // Dependencies
  val tempoTapCalculator = remember { TempoTapCalculator() }

  // Callbacks
  val fetchVideoInfo: (String) -> Unit = { url ->
    val currentUiState =
        uiState.copy(
            loading = true,
            popup =
                if (uiState.popup == UiState.PopupType.VIDEO_URL) UiState.PopupType.NONE
                else uiState.popup)

    composableScope.launch(Dispatchers.IO) {
      try {
        if (!validateYoutubeUrl(url)) {
          throw Exception("Invalid url: $url")
        }

        setUiState(currentUiState)

        val getUrlRequest = YoutubeDLRequest(url)
        getUrlRequest.addOption("-f", "b")
        getUrlRequest.addOption("--no-playlist")

        val info = YoutubeDL.getInstance().getInfo(getUrlRequest)
        if (info.url != null) {
          rootState = rootState.copy(videoInfo = info, loopRangeMs = 0L..(info.duration * 1000))
        } else {
          throw Exception("Unable to fetch url: $url. Source url is missing")
        }
      } catch (e: Exception) {
        val errMsg =
            when (e) {
              is YoutubeDLException -> "Unable to fetch url: $url"
              is InterruptedException -> "Song fetch interrupted"
              is YoutubeDL.CanceledException -> "Song fetch canceled"
              else -> e.message ?: "Unknown error"
            }
        Log.e("YoutubeDL", errMsg)
        Handler(Looper.getMainLooper()).post {
          Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
        }
      }

      setUiState(currentUiState.copy(loading = false))
    }
  }

  // Metronome
  val metronomeSoundPool = remember {
    SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
        .build()
  }
  val soundId = remember { metronomeSoundPool.load(context, R.raw.metronome_click, 1) }

  DisposableEffect(metronomeSoundPool) { onDispose { metronomeSoundPool.release() } }

  LaunchedEffect(rootState, uiState.playing) {
    val videoInfo = rootState.videoInfo
    val player = rootState.player

    if (videoInfo != null && player != null && uiState.playing) {
      val metronome = Metronome(rootState.tempo)

      withContext(Dispatchers.Main) {
        while (true) {
          if (metronome.tick(player.currentPosition)) {
            metronomeSoundPool.play(soundId, 1f, 1f, 1, 0, 1f)
          }
          delay(10)
        }
      }
    }
  }

  // Render
  Column(Modifier.padding(8.dp).fillMaxHeight()) {
    ControlPanel(
        uiState = uiState,
        onUiStateChange = setUiState,
        rootState = rootState,
        onRootStateChange = { rootState = it })

    var loopRange by remember { mutableStateOf(0L..0L) }

    LaunchedEffect(rootState.videoInfo) {
      rootState.videoInfo?.let { loopRange = 0L..(it.duration * 1000) }
    }

    val videoInfo = rootState.videoInfo
    if (videoInfo != null) {
      // Song view
      Column(
          Modifier.padding(horizontal = 128.dp, vertical = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween) {
            VideoPlayer(
                url = videoInfo.url!!,
                loopRange = rootState.loopRangeMs,
                tempo = 120f,
                playing = uiState.playing,
                onPlayingSet = { setUiState(uiState.copy(playing = it)) },
                onPlayerSet = { rootState = rootState.copy(player = it) })
            LoopRangeSelector(
                range = loopRange,
                maxDurationMs = videoInfo.duration * 1000,
                onRangeChange = { loopRange = it },
                onChangeFinished = { rootState = rootState.copy(loopRangeMs = loopRange) })
          }
    } else {
      // Init view
      Spacer(Modifier.height(8.dp))
      Surface(
          tonalElevation = 0.dp,
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).fillMaxSize()) {
                  if (uiState.loading) {
                    Loading()
                  } else {
                    VideoUrlInput(fetchVideoInfo, Modifier.width(350.dp))
                  }
                }
          }
    }
  }

  val hidePopup = { setUiState(uiState.copy(popup = UiState.PopupType.NONE)) }

  when (uiState.popup) {
    UiState.PopupType.VIDEO_URL -> {
      Dialog(onDismissRequest = hidePopup) { Card { VideoUrlInput(onSubmit = fetchVideoInfo) } }
    }
    UiState.PopupType.TEMPO_PICKER -> {
      var tempo by remember { mutableStateOf(Tempo()) }

      Dialog(onDismissRequest = hidePopup) {
        Card {
          TempoPicker(
              tempo = rootState.tempo,
              onTempoChange = {
                rootState = rootState.copy(tempo = it)
                hidePopup()
              },
              tempoTapValue = tempo,
              onTempoTap = {
                tempoTapCalculator.tap(rootState.player?.currentPosition ?: 0L)
                tempo =
                    tempo.copy(
                        msPerBeat = tempoTapCalculator.msPerBeat.toDouble(),
                        msOffset = tempoTapCalculator.msOffset.toDouble())
              })
        }
      }
    }
    UiState.PopupType.NONE -> Unit
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
  PlayAlongTheme { MainContainer() }
}
