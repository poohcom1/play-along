package com.poohcom1.playalong.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(millis: Long, showHours: Boolean): String {
  val pattern = if (showHours) "H:mm:ss" else "m:ss"
  val sdf = SimpleDateFormat(pattern, Locale.getDefault())
  return sdf.format(Date(millis))
}
