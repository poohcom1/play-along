package com.poohcom1.playalong.ui.components

import android.content.res.ColorStateList
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Loading(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
  val progressSpinner = remember {
    val progressBar = ProgressBar(context)
    progressBar.isIndeterminate = true
    progressBar.indeterminateTintList = ColorStateList.valueOf(primaryColor)
    progressBar
  }
  Column(
      modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        AndroidView(factory = { progressSpinner })
        Text("Loading...")
      }
}
