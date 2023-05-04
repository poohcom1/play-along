package com.poohcom1.playalong.ui.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.poohcom1.playalong.R
import com.poohcom1.playalong.states.RootState
import com.poohcom1.playalong.states.UiState
import com.poohcom1.playalong.ui.components.LoopRangeSelector
import com.poohcom1.playalong.ui.components.VerticalDivider
import com.yausername.youtubedl_android.mapper.VideoInfo

@Composable
fun ControlPanel(
    uiState: UiState,
    onUiStateChange: (UiState) -> Unit,
    rootState: RootState,
    onRootStateChange: (RootState) -> Unit,
) {
  val density = LocalDensity.current

  AnimatedVisibility(
      visible = uiState.controllerVisible,
      enter = slideInVertically { with(density) { -100.dp.roundToPx() } },
      exit = slideOutVertically { with(density) { -100.dp.roundToPx() } }) {
        Column(Modifier.padding(16.dp)) {
          // Top Control panel
          Surface(tonalElevation = 25.dp, shape = MaterialTheme.shapes.medium) {
            Row(
                Modifier.height(50.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                  // Left
                  Row() {
                    TempoPanel(
                        metronomeOn = rootState.metronomeOn,
                        onMetronomeOn = { onRootStateChange(rootState.copy(metronomeOn = it)) },
                        tempo = rootState.bpm.toLong(),
                        onShowTempoPicker = {
                          onUiStateChange(uiState.copy(popup = UiState.PopupType.TEMPO_PICKER))
                        },
                    )
                  }

                  VerticalDivider(Modifier.weight(0.05f))

                  // Center
                  Row(Modifier.weight(0.5f)) {
                    LoopPanel(
                        loopRange = rootState.loopRangeMs,
                        onLoopRangeChange = { onRootStateChange(rootState.copy(loopRangeMs = it)) },
                        loopOn = rootState.loopOn,
                        onLoopOnSet = { onRootStateChange(rootState.copy(loopOn = it)) },
                        videoInfo = rootState.videoInfo)
                  }

                  VerticalDivider(Modifier.weight(0.05f))

                  // Right
                  IconButton(
                      onClick = {
                        onUiStateChange(uiState.copy(popup = UiState.PopupType.VIDEO_URL))
                      },
                  ) {
                    Icon(Icons.Filled.MusicVideo, contentDescription = "Load Video")
                  }
                }
          }
        }
      }
}

@Composable
private fun TempoPanel(
    metronomeOn: Boolean,
    onMetronomeOn: (Boolean) -> Unit,
    tempo: Long,
    onShowTempoPicker: () -> Unit,
) {
  val context = LocalContext.current

  Row(horizontalArrangement = Arrangement.Start) {
    IconButton(modifier = Modifier.padding(8.dp), onClick = { onMetronomeOn(!metronomeOn) }) {
      Icon(
          ContextCompat.getDrawable(context, R.drawable.metronome)
              ?.toBitmap(128, 128)
              ?.asImageBitmap()!!,
          contentDescription = "Metronome",
          tint =
              if (metronomeOn) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.onSurface)
    }

    TextButton(onClick = onShowTempoPicker) {
      Text(
          text = "BPM",
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
          fontSize = 12.sp)
      Spacer(modifier = Modifier.padding(2.dp))
      Text(text = tempo.toString())
    }
  }
}

@Composable
private fun LoopPanel(
    loopRange: LongRange,
    onLoopRangeChange: (LongRange) -> Unit,
    loopOn: Boolean,
    onLoopOnSet: (Boolean) -> Unit,
    videoInfo: VideoInfo?,
) {
  Row(horizontalArrangement = Arrangement.Center) {
    var tempRange by remember { mutableStateOf(loopRange) }

    IconButton(
        onClick = { onLoopOnSet(!loopOn) },
    ) {
      Icon(
          Icons.Filled.Loop,
          contentDescription = "Load Video",
          tint =
              if (loopOn) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.onSurface)
    }

    LoopRangeSelector(
        range = tempRange,
        onRangeChange = { tempRange = it },
        maxDurationMs = (videoInfo?.duration ?: 0) * 1000,
        onChangeFinished = { onLoopRangeChange(tempRange) },
        disabled = !loopOn)
  }
}
