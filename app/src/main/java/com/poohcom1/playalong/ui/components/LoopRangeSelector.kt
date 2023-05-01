package com.poohcom1.playalong.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poohcom1.playalong.utils.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoopRangeSelector(
    modifier: Modifier = Modifier,
    range: LongRange,
    onRangeChange: (LongRange) -> Unit,
    maxDurationMs: Int,
    onChangeFinished: () -> Unit = {},
) {
  val showHours = maxDurationMs > 3600000

  Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier) {
        Text(text = formatTimestamp(range.first, showHours = showHours))
        RangeSlider(
            value = range.first.toFloat()..range.last.toFloat(),
            onValueChange = { onRangeChange(it.start.toLong()..it.endInclusive.toLong()) },
            onValueChangeFinished = onChangeFinished,
            valueRange = 0f..maxDurationMs.toFloat(),
            modifier = Modifier.weight(1f),
        )
        Text(text = formatTimestamp(range.last, showHours = showHours))
      }
}
