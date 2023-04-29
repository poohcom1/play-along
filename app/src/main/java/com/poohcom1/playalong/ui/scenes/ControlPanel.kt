package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poohcom1.playalong.viewmodels.UiState

@Composable
fun ControlPanel(uiState: UiState, onRootStateChanged: (UiState) -> Unit) {
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.Center) {

        }
        Row(horizontalArrangement = Arrangement.Center) {
            IconButton(modifier = Modifier.weight(1f),
                onClick = { onRootStateChanged(uiState.copy(playing = !uiState.playing)) }) {
                if (uiState.playing) {
                    Icon(Icons.Filled.Pause, contentDescription = "Pause")
                } else {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {

        }
    }
}
