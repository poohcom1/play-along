package com.poohcom1.playalong.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.poohcom1.playalong.ui.theme.PlayAlongTheme

@Composable
fun VideoPlayer(url: String) {

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PlayAlongTheme {
        VideoPlayer("https://www.youtube.com/watch?v=te6JsCBrZn0")
    }
}