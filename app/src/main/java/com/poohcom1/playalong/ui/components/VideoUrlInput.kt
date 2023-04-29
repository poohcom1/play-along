package com.poohcom1.playalong.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoUrlInput(onSubmit: (String) -> Unit, modifier: Modifier = Modifier) {
  val clipboardManager: ClipboardManager = LocalClipboardManager.current

  var youtubeUrl by rememberSaveable { mutableStateOf("") }

  Column(modifier = modifier.padding(16.dp)) {
    OutlinedTextField(
        value = youtubeUrl,
        onValueChange = { youtubeUrl = it },
        placeholder = { Text(text = "Enter youtube URL...") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(2.dp))
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
      IconButton(
          onClick = {
            if (clipboardManager.hasText()) {
              youtubeUrl = clipboardManager.getText().toString()
            }
          }) {
            Icon(Icons.Filled.ContentPaste, contentDescription = "Paste url")
          }
      Button(onClick = { onSubmit(youtubeUrl) }, enabled = youtubeUrl.isNotEmpty()) {
        Text(text = "Load")
      }
    }
  }
}
