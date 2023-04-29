package com.poohcom1.playalong.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoUrlInput(onSubmit: (String) -> Unit, modifier: Modifier = Modifier) {
  var youtubeUrl by remember { mutableStateOf("") }

  Column(modifier = modifier.padding(16.dp)) {
    OutlinedTextField(
        value = youtubeUrl,
        onValueChange = { youtubeUrl = it },
        placeholder = { Text(text = "Enter youtube URL...") },
        singleLine = true,
    )
    Button(onClick = { onSubmit(youtubeUrl) }, enabled = youtubeUrl.isNotEmpty()) {
      Text(text = "Load")
    }
  }
}
