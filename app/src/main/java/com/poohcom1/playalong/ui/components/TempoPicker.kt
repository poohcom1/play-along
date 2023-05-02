package com.poohcom1.playalong.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.utils.TempoTapCalculator
import com.poohcom1.playalong.utils.bpmToMsPerBeat
import com.poohcom1.playalong.utils.msPerBeatToBpm
import kotlin.math.roundToInt

@Composable
fun TempoPicker(
    // tempo picker
    tempo: Tempo,
    onTempoChange: (Tempo) -> Unit,
    tempoRange: IntRange = 5..240,
    onDismiss: () -> Unit,
    player: ExoPlayer? = null,
) {
  val context = LocalContext.current
  val tempoTapCalculator = remember { TempoTapCalculator() }

  val numberPicker = remember {
    NumberPicker(context).apply {
      minValue = tempoRange.first
      maxValue = tempoRange.last
      value = msPerBeatToBpm(tempo.msPerBeat).toInt()
      setOnValueChangedListener { _, _, newVal ->
        onTempoChange(tempo.copy(msPerBeat = bpmToMsPerBeat(newVal.toDouble())))
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
                tempoTapCalculator.tap(player?.currentPosition ?: 0)
                onTempoChange(tempoTapCalculator.tempo)
                numberPicker.value = msPerBeatToBpm(tempoTapCalculator.msPerBeat.toDouble()).toInt()
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
            val roundedMsPerBeat = tempo.msPerBeat.roundToInt()

            Text(text = "Offset", color = labelColor, fontSize = labelSize)

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                  Slider(
                      value = tempo.msOffset.toFloat(),
                      onValueChange = { onTempoChange(tempo.copy(msOffset = it.toDouble())) },
                      valueRange = -roundedMsPerBeat.toFloat()..roundedMsPerBeat.toFloat(),
                      steps = (roundedMsPerBeat * 2) / 10)

                  Text(text = "${tempo.msOffset.roundToInt()} ms")
                }
          }
    }

    Button(onClick = { onDismiss() }) { Text(text = "Done") }
  }
}
