package com.poohcom1.playalong.utils

fun validateYoutubeUrl(url: String): Boolean {
  return url.matches(Regex("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$"))
}
