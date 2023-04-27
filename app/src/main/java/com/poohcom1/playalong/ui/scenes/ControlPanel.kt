package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poohcom1.playalong.models.ControlPanelSettings

@Composable
fun ControlPanel(setting: ControlPanelSettings, onSettingChanged: (ControlPanelSettings) -> Unit) {
    Column(
        Modifier.height(50.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Column {

        }
        Column {
            IconButton(onClick = { onSettingChanged(setting.copy(playing = !setting.playing)) }) {
                if (setting.playing) {
                    Icon(Icons.Filled.Pause, contentDescription = "Pause")
                } else {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                }
            }
        }
        Column {

        }
    }
}