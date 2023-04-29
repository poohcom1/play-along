package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoUrlPopup(onDismiss: () -> Unit, onUrlSubmit: (String) -> Unit) {
    var youtubeUrl by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card() {
            Column(Modifier.padding(16.dp)) {
                TextField(
                    value = youtubeUrl,
                    onValueChange = { youtubeUrl = it },
                    placeholder = { Text(text = "Enter URL") },
                    singleLine = true,
                )
                Button(onClick = { onUrlSubmit(youtubeUrl) }) {
                    Text(text = "Submit")
                }
            }
        }
    }
}