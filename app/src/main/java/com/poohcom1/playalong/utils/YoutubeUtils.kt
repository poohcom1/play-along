package com.poohcom1.playalong.utils

import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo

fun validateYoutubeUrl(url: String): Boolean {
  return url.matches(Regex("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$"))
}

fun getYtdlVideoInfo(url: String): Result<VideoInfo> {
  try {
    if (!validateYoutubeUrl(url)) {
      throw Exception("Invalid url: $url")
    }

    val getUrlRequest = YoutubeDLRequest(url)
    getUrlRequest.addOption("-f", "b")
    getUrlRequest.addOption("--no-playlist")

    val info = YoutubeDL.getInstance().getInfo(getUrlRequest)
    if (info.url != null) {
      return Result.success(info)
    } else {
      throw Exception("Unable to fetch url: $url. Source url is missing")
    }
  } catch (e: Exception) {
    val errMsg =
        when (e) {
          is YoutubeDLException -> "Unable to fetch url: $url"
          is InterruptedException -> "Song fetch interrupted"
          is YoutubeDL.CanceledException -> "Song fetch canceled"
          else -> e.message ?: "Unknown error"
        }

    return Result.failure(Exception(errMsg))
  }
}
