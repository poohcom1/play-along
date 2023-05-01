// package com.poohcom1.playalong.ui.scenes
//
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Spacer
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.padding
// import androidx.compose.material3.ExperimentalMaterial3Api
// import androidx.compose.material3.RangeSlider
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.setValue
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import com.poohcom1.playalong.ui.components.VideoPlayer
// import com.poohcom1.playalong.viewmodels.UiState
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun PlayerController(
//    url: String,
//    duration: Int,
//    uiState: UiState,
//    onRootStateChanged: (UiState) -> Unit
// ) {
//  var loopRange: LongRange by remember { mutableStateOf(0L..duration * 1000) }
//
//  var isSeeking by remember { mutableStateOf(false) }
//  var wasPlayingBeforeSeek by remember { mutableStateOf(false) }
//
//  Column(
//      Modifier.padding(horizontal = 128.dp, vertical = 16.dp),
//      horizontalAlignment = Alignment.CenterHorizontally) {
//        Column {
//          VideoPlayer(
//              url,
//              loopRange,
//              120f,
//              playing = uiState.playing,
//              onPlayingSet = { onRootStateChanged(uiState.copy(playing = it)) },
//              onPlayerSet = { onRootStateChanged(uiState.copy(player = it)) })
//          Spacer(Modifier.height(16.dp))
//          RangeSlider(
//              value = (loopRange.first / 1000).toFloat()..(loopRange.last / 1000).toFloat(),
//              onValueChange = {
//                if (!isSeeking) {
//                  isSeeking = true
//                  if (uiState.playing) {
//                    wasPlayingBeforeSeek = true
//                    onRootStateChanged(uiState.copy(playing = false))
//                  }
//                }
//                loopRange = it.start.toLong() * 1000..it.endInclusive.toLong() * 1000
//              },
//              valueRange = 0f..duration.toFloat(),
//              onValueChangeFinished = {
//                isSeeking = false
//                if (wasPlayingBeforeSeek) {
//                  wasPlayingBeforeSeek = false
//                  onRootStateChanged(uiState.copy(playing = true))
//                }
//              },
//              steps = duration,
//          )
//        }
//      }
// }
