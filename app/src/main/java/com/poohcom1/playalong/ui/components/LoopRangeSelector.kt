package com.poohcom1.playalong.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poohcom1.playalong.utils.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoopRangeSelector(
    modifier: Modifier = Modifier,
    range: LongRange,
    onRangeChange: (LongRange) -> Unit,
    maxDurationMs: Int,
    onChangeFinished: () -> Unit = {},
    disabled: Boolean = false,
) {
  val showHours = maxDurationMs > 3600000

  Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier) {
        Text(
            text =
                "${formatTimestamp(range.first, showHours = showHours)} - ${formatTimestamp(range.last, showHours = showHours)}",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (disabled) 0.5f else 0.8f),
            fontSize = 12.sp)

        Spacer(Modifier.padding(horizontal = 8.dp))

        RangeSlider(
            value = range.first.toFloat()..range.last.toFloat(),
            onValueChange = { onRangeChange(it.start.toLong()..it.endInclusive.toLong()) },
            onValueChangeFinished = onChangeFinished,
            valueRange = 0f..maxDurationMs.toFloat(),
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(inactiveTrackColor = Color.Black.copy(alpha = 0.2f)),
            enabled = !disabled,
        )
      }
}
