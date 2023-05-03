package com.poohcom1.playalong.ui.components

import android.content.res.ColorStateList
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Loading(modifier: Modifier = Modifier) {
  val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

  Column(
      modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        AndroidView(
            factory = { context ->
              ProgressBar(context).apply {
                isIndeterminate = true
                indeterminateTintList = ColorStateList.valueOf(primaryColor)
              }
            })
        Text("Loading...")
      }
}
