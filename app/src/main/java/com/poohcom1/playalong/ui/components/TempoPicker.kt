package com.poohcom1.playalong.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.utils.bpmToMsPerBeat
import com.poohcom1.playalong.utils.msPerBeatToBpm
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempoPicker(
    // tempo picker
    tempo: Tempo,
    onTempoChange: (Tempo) -> Unit,
    tempoRange: IntRange = 5..240,
    // tempo tapper
    tempoTapValue: Tempo,
    onTempoTap: () -> Unit,
) {
  val context = LocalContext.current
  var currentTempo by remember { mutableStateOf(tempo) }

  val numberPicker = remember {
    NumberPicker(context).apply {
      minValue = tempoRange.first
      maxValue = tempoRange.last
      value = msPerBeatToBpm(currentTempo.msPerBeat).toInt()
      setOnValueChangedListener { _, _, newVal ->
        currentTempo = currentTempo.copy(msPerBeat = bpmToMsPerBeat(newVal.toDouble()))
      }
    }
  }

  Column(Modifier.padding(32.dp, 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    // Tapper
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
          OutlinedButton(
              onClick = {
                onTempoTap()
                currentTempo = tempoTapValue
                numberPicker.value = msPerBeatToBpm(currentTempo.msPerBeat).toInt()
              },
              shape = MaterialTheme.shapes.medium) {
                Text(text = "Tap tempo")
              }
        }

    Divider(Modifier.padding(4.dp, 8.dp))

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      val labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
      val labelSize = MaterialTheme.typography.labelSmall.fontSize

      // BPM
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Top) {
            Text(text = "BPM", color = labelColor, fontSize = labelSize)

            Column(verticalArrangement = Arrangement.Center) {
              AndroidView(factory = { numberPicker })
            }
          }

      // Offset
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Top) {
            val roundedMsPerBeat = currentTempo.msPerBeat.roundToInt()

            Text(text = "Offset", color = labelColor, fontSize = labelSize)

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                  Slider(
                      value = currentTempo.msOffset.toFloat(),
                      onValueChange = {
                        currentTempo = currentTempo.copy(msOffset = it.toDouble())
                      },
                      valueRange = -roundedMsPerBeat.toFloat()..roundedMsPerBeat.toFloat(),
                      steps = (roundedMsPerBeat * 2) / 10)

                  Text(text = "${currentTempo.msOffset.roundToInt()} ms")
                }
          }
    }

    Button(onClick = { onTempoChange(currentTempo) }) { Text(text = "Set Tempo") }
  }
}
