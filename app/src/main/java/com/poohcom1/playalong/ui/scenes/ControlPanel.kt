package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poohcom1.playalong.viewmodels.PopupType
import com.poohcom1.playalong.viewmodels.RootState
import com.poohcom1.playalong.viewmodels.UiState

@Composable
fun ControlPanel(
    uiState: UiState,
    onUiStateChange: (UiState) -> Unit,
    rootState: RootState,
) {
  Surface(tonalElevation = 25.dp, shape = MaterialTheme.shapes.medium) {
    Row(Modifier.height(50.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
      val modifier = Modifier.weight(1f / 3f)

      Row(modifier, horizontalArrangement = Arrangement.Start) {}

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
            onClick = { onUiStateChange(uiState.copy(popup = PopupType.VIDEO_URL)) },
        ) {
          Icon(Icons.Filled.MusicVideo, contentDescription = "Load Video")
        }
      }
    }
  }
}
