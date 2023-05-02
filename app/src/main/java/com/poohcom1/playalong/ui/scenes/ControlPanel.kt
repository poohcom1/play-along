package com.poohcom1.playalong.ui.scenes

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.poohcom1.playalong.R
import com.poohcom1.playalong.viewmodels.RootState
import com.poohcom1.playalong.viewmodels.UiState

@Composable
fun ControlPanel(
    uiState: UiState,
    onUiStateChange: (UiState) -> Unit,
    rootState: RootState,
    onRootStateChange: (RootState) -> Unit,
) {
  val context = LocalContext.current

  val mediaPlayer = remember { MediaPlayer.create(context, R.raw.metronome_click) }

  DisposableEffect(mediaPlayer) { onDispose { mediaPlayer.release() } }

  Surface(tonalElevation = 25.dp, shape = MaterialTheme.shapes.medium) {
    Row(Modifier.height(50.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
      val modifier = Modifier.weight(1f / 3f)

      Row(modifier, horizontalArrangement = Arrangement.Start) {
        IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = { onRootStateChange(rootState.copy(metronomeOn = !rootState.metronomeOn)) }) {
              Icon(
                  ContextCompat.getDrawable(context, R.drawable.metronome)
                      ?.toBitmap(128, 128)
                      ?.asImageBitmap()!!,
                  contentDescription = "Metronome",
                  tint =
                      if (rootState.metronomeOn) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.onSurface)
            }

        TextButton(
            onClick = { onUiStateChange(uiState.copy(popup = UiState.PopupType.TEMPO_PICKER)) }) {
              Text(
                  text = "BPM",
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                  fontSize = 12.sp)
              Spacer(modifier = Modifier.padding(2.dp))
              Text(text = rootState.bpm.toInt().toString())
            }
      }

      Row(modifier, horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = {
              if (rootState.player != null) {
                onUiStateChange(uiState.copy(playing = !uiState.playing))
              }
            }) {
              if (uiState.playing) {
                Icon(Icons.Filled.Pause, contentDescription = "Pause")
              } else {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
              }
            }
      }
      Row(modifier, horizontalArrangement = Arrangement.End) {
        IconButton(
            onClick = { onUiStateChange(uiState.copy(popup = UiState.PopupType.VIDEO_URL)) },
        ) {
          Icon(Icons.Filled.MusicVideo, contentDescription = "Load Video")
        }
      }
    }
  }
}
