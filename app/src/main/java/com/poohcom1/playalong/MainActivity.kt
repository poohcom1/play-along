package com.poohcom1.playalong

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.core.view.WindowCompat
import com.poohcom1.playalong.states.RootState
import com.poohcom1.playalong.states.UiState
import com.poohcom1.playalong.ui.components.Loading
import com.poohcom1.playalong.ui.components.TempoPicker
import com.poohcom1.playalong.ui.components.VideoPlayer
import com.poohcom1.playalong.ui.components.VideoUrlInput
import com.poohcom1.playalong.ui.media.Metronome
import com.poohcom1.playalong.ui.scenes.ControlPanel
import com.poohcom1.playalong.ui.theme.PlayAlongTheme
import com.poohcom1.playalong.utils.getYtdlVideoInfo
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import kotlinx.coroutines.Dispatchers
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
          MainComponent()
        }
      }
    }

    hideSystemUI()
  }

  /**
   * Go fullscreen See:
   * https://medium.com/@bhuvanesh_shan/making-full-screen-ui-in-android-jetpack-compose-46a7a1362b02
   */
  private fun hideSystemUI() {
    // Hides the ugly action bar at the top
    actionBar?.hide()

    // Hide the status bars

    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
      @Suppress("DEPRECATION") window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
      window.insetsController?.apply {
        hide(WindowInsets.Type.statusBars())
        systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      }
    }
  }
}

@Composable
fun MainComponent() {
  val context = LocalContext.current
  val composableScope = rememberCoroutineScope()

  // States
  var uiState by remember { mutableStateOf(UiState()) }
  var rootState by remember { mutableStateOf(RootState()) }

  // Callbacks
  val fetchVideoInfo: (String) -> Unit = { url ->
    rootState = rootState.copy(loading = true)
    composableScope.launch(Dispatchers.IO) {
      getYtdlVideoInfo(url)
          .onSuccess { videoInfo ->
            rootState =
                rootState.copy(videoInfo = videoInfo, loopRangeMs = 0L..videoInfo.duration * 1000)
          }
          .onFailure { throwable ->
            withContext(Dispatchers.Main) {
              Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
            }
          }

      rootState = rootState.copy(loading = false)
    }
  }

  Metronome(
      videoInfo = rootState.videoInfo,
      player = rootState.player,
      tempo = rootState.tempo,
      playing = rootState.metronomeOn && rootState.playing)

  // Render
  var loopRange by remember { mutableStateOf(0L..0L) }

  LaunchedEffect(rootState.videoInfo) {
    rootState.videoInfo?.let { loopRange = 0L..(it.duration * 1000) }
  }

  val videoInfo = rootState.videoInfo
  if (videoInfo != null && !rootState.loading) {
    // Song view

    Box {
      VideoPlayer(
          url = videoInfo.url!!,
          loopRange = rootState.loopRangeMs,
          loopOn = rootState.loopOn,
          tempo = 120f,
          playing = rootState.playing,
          onPlayingSet = { rootState = rootState.copy(playing = it) },
          onPlayerSet = { rootState = rootState.copy(player = it) },
          onControllerVisibilitySet = { uiState = uiState.copy(controllerVisible = it) })
      ControlPanel(
          uiState = uiState,
          onUiStateChange = { uiState = it },
          rootState = rootState,
          onRootStateChange = { rootState = it })
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
              modifier = Modifier.fillMaxSize()) {
                if (rootState.loading) {
                  Loading()
                } else {
                  VideoUrlInput(fetchVideoInfo, Modifier.width(350.dp))
                }
              }
        }
  }

  val hidePopup = { uiState = uiState.copy(popup = UiState.PopupType.NONE) }

  when (uiState.popup) {
    UiState.PopupType.VIDEO_URL -> {
      Dialog(onDismissRequest = hidePopup) {
        Card {
          VideoUrlInput(
              onSubmit = {
                hidePopup()
                fetchVideoInfo(it)
              })
        }
      }
    }
    UiState.PopupType.TEMPO_PICKER -> {
      Dialog(onDismissRequest = hidePopup) {
        Card {
          TempoPicker(
              tempo = rootState.tempo,
              onTempoChange = { rootState = rootState.copy(tempo = it) },
              onDismiss = hidePopup,
              player = rootState.player)
        }
      }
    }
    UiState.PopupType.NONE -> Unit
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
  PlayAlongTheme { MainComponent() }
}
