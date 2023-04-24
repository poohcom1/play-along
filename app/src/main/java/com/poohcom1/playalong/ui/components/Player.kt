package com.poohcom1.playalong.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.poohcom1.playalong.ui.components.ui.theme.PlayAlongTheme

@Composable
fun Player(url: String, modifier: Modifier = Modifier) {
    Text(
        text = "$url!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PlayAlongTheme {
        Player("Android")
    }
}